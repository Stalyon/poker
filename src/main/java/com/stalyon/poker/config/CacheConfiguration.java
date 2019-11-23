package com.stalyon.poker.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.stalyon.poker.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.stalyon.poker.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.stalyon.poker.domain.User.class.getName());
            createCache(cm, com.stalyon.poker.domain.Authority.class.getName());
            createCache(cm, com.stalyon.poker.domain.User.class.getName() + ".authorities");
            createCache(cm, com.stalyon.poker.domain.ParseHistory.class.getName());
            createCache(cm, com.stalyon.poker.domain.Game.class.getName());
            createCache(cm, com.stalyon.poker.domain.Player.class.getName());
            createCache(cm, com.stalyon.poker.domain.Hand.class.getName());
            createCache(cm, com.stalyon.poker.domain.PlayerAction.class.getName());
            createCache(cm, com.stalyon.poker.domain.ShowDown.class.getName());
            createCache(cm, com.stalyon.poker.domain.Tournoi.class.getName());
            createCache(cm, com.stalyon.poker.domain.SitAndGo.class.getName());
            createCache(cm, com.stalyon.poker.domain.CashGame.class.getName());
            createCache(cm, com.stalyon.poker.domain.GameHistory.class.getName());
            createCache(cm, com.stalyon.poker.domain.PlayerHand.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}

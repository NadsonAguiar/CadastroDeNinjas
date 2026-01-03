package dev.nadsonaguiar.CadastroDeNinjas.Config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

// Configuration = “Spring, essa classe define Beans de configuração”
@Configuration
// EnableCaching = Liga o sistema de cache do Spring, sem isso as Annotation não funcionam para cache
@EnableCaching
public class RedisConfig {

    @Bean // RedisCacheManager = “Spring, EU quero controlar como o cache funciona”
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){

        // 🔹 Cache padrão (fallback)
        // RedisCacheConfiguration = configuração base do cache
        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        // entryTtl = Todos os itens salvo no Redis expira em 10 minutos
                        .entryTtl(Duration.ofMinutes(10))
                        /* serializeValuesWith = Antes de salvar no Redis, converta o objeto Java em JSON, e quando buscar:
                        “Converta o JSON de volta para objeto Java” */
                        .serializeValuesWith(
                                // RedisSerializationContext = Manual de como ler/escrever dados no Redis, ele define como serializar KEY e VALUE
                                RedisSerializationContext.SerializationPair // SerializationPair = Redis precisa de 2 coisas: Como salvar e ler, o serializationPair (serializer de escrita + serializer de leitura)
                                        // fromSerializer =  “Use esse mesmo serializer para salvar e ler”
                                        .fromSerializer(new GenericJackson2JsonRedisSerializer()) // GenericJackson2JsonRedisSerializer(heroi): na ida Objeto Java -> JSON -> byte[], na volta byte[] -> JSON -> Java
                        );


        // 🔹 Configurações específicas
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();

        // Cache individual (id)
        cacheConfigs.put("ninjas",
                defaultConfig.entryTtl(Duration.ofMinutes(10)));

        // Cache de lista
        cacheConfigs.put("ninjasList",
                defaultConfig.entryTtl(Duration.ofMinutes(5)));

        // “Use essa configuração para TODOS os caches”
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}

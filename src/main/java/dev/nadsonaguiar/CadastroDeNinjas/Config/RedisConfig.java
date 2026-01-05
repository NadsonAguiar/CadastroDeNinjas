package dev.nadsonaguiar.CadastroDeNinjas.Config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dev.nadsonaguiar.CadastroDeNinjas.Ninjas.NinjaDTO;
import dev.nadsonaguiar.CadastroDeNinjas.Ninjas.PageDTO;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Configuration = “Spring, essa classe define Beans de configuração”
@Configuration
// EnableCaching = Liga o sistema de cache do Spring, sem isso as Annotation não funcionam para cache
@EnableCaching
public class RedisConfig {

    @Bean // RedisCacheManager = “Spring, EU quero controlar como o cache funciona”
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){

        // Serializador para List<NinjaDTO>
        JavaType listType = TypeFactory.defaultInstance()
                .constructCollectionType(List.class, NinjaDTO.class);
        Jackson2JsonRedisSerializer<List<NinjaDTO>> listSerializer = new Jackson2JsonRedisSerializer<>(listType);

        // Cache Individual (Ninja DTO)
        // RedisCacheConfiguration = configuração base do cache
        RedisCacheConfiguration ninjaById =
                RedisCacheConfiguration.defaultCacheConfig()
                        // entryTtl = Tempo de duração no cache
                        .entryTtl(Duration.ofMinutes(10))
                        /* serializeValuesWith = Antes de salvar no Redis, converta o objeto Java em JSON, e quando buscar:
                        “Converta o JSON de volta para objeto Java” */
                        .serializeValuesWith(
                                // RedisSerializationContext = Manual de como ler/escrever dados no Redis, ele define como serializar KEY e VALUE
                                RedisSerializationContext.SerializationPair // SerializationPair = Redis precisa de 2 coisas: Como salvar e ler, o serializationPair (serializer de escrita + serializer de leitura)
                                        // fromSerializer =  “Use esse mesmo serializer para salvar e ler”
                                        .fromSerializer(new Jackson2JsonRedisSerializer<>(NinjaDTO.class))

                        )
                        .disableCachingNullValues();

        // Cache de listas (List<NinjaDTO>)
        RedisCacheConfiguration listConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(listSerializer)
                )
                .disableCachingNullValues();

        // Cache de paginação (PageDTO)
        RedisCacheConfiguration pageConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new Jackson2JsonRedisSerializer<>(PageDTO.class))
                )
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                "ninjas-by-id", ninjaById,
                "ninjas-list", listConfig,
                "ninjas-filter", listConfig,
                "ninjas-page", pageConfig
        );

            return RedisCacheManager.builder(connectionFactory)
                    .cacheDefaults(ninjaById)
                    .withInitialCacheConfigurations(cacheConfigs)
                    .build();
        }
}

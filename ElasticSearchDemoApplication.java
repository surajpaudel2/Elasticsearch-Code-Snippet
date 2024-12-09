package com.suraj.carrercraft.elasticsearchdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.suraj.carrercraft.elasticsearchdemo.repository")
public class ElasticSearchDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchDemoApplication.class, args);
    }

}

package com.suraj.carrercraft.elasticsearchdemo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.suraj.carrercraft.elasticsearchdemo.model.Product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ElasticsearchClient elasticsearchClient;

    public ProductService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    /*
        Method to save the product in elasticsearch
     */
    public void saveProduct(Product product) {
        try {
            elasticsearchClient.index(IndexRequest.of(i -> i
                    .index("products")
                    .id(product.getId())
                    .document(product)
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Fuzzy search in only one value
     */
    public List<Product> fuzzySearch(String nameKeyword) {
        try {
            // Create the fuzzy match query
            MatchQuery matchQuery = MatchQuery.of(m -> m
                    .field("name")
                    .query(nameKeyword)
                    .fuzziness("AUTO")
                    .prefixLength(3)
            );

            // Execute the search query
            SearchResponse<Product> response = elasticsearchClient.search(
                    s -> s.index("products") // Replace with your index name
                            .query(q -> q.match(matchQuery)),
                    Product.class
            );

            // Extract the results
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to perform fuzzy search", e);
        }
    }


    /*
        -> Fuzzy search in multiple values, for example if user enters the laptop then laptop is searched in
        category, name and description as well.
     */
    public List<Product> searchByKeyword(String keyword) {
        try {
            // Create the multi-match query
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m
                    .query(keyword)
                    .fields("name", "category", "description") // Specify the fields to search
                    .fuzziness("AUTO")                         // Add fuzziness for approximate matching
                    .prefixLength(3)                           // Ignore fuzziness for the first 3 characters
            );

            // Execute the search query
            SearchResponse<Product> response = elasticsearchClient.search(
                    s -> s.index("products") // Replace with your index name
                            .query(q -> q.multiMatch(multiMatchQuery)),
                    Product.class
            );

            // Extract the results
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to perform multi-field search", e);
        }
    }

}

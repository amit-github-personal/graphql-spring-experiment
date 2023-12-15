package com.example.graphql;

import com.example.graphql.client.*;
import com.example.graphql.types.Artist;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import graphql.ExecutionResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {ShowsDatafetcher.class, DgsAutoConfiguration.class})
public class ShowsDataFetcherTests {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void shows() {
        List<String> titles = dgsQueryExecutor.
                executeAndExtractJsonPath("{ shows { title releaseYear }}",
                        "data.shows[*].title");
        Assertions.assertThat(titles).contains("Ozark");
    }

    @Test
    void showsQWithGraphQLQueryAPI() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new ShowsGraphQLQuery.Builder().titleFilter("Oz").build(),
                new ShowsProjectionRoot().title()
        );

        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(request.serialize(), "data.shows[*].title");
        Assertions.assertThat(titles.contains("Ozark"));
    }

    @Test
    void parentChildGraphQLQueryAPI() {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                new ShowsByArtistGraphQLQuery.Builder().artistFilter("Sonu Nigam").build(),
                new ShowsByArtistProjectionRoot<>().artist().name()
        );
        List<String> execute = dgsQueryExecutor.executeAndExtractJsonPath(request.serialize(), "data.showsByArtist[*].artist.name");
        Assertions.assertThat(execute.size() == 2);
    }

}

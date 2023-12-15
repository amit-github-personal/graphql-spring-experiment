package com.example.graphql;

import com.example.graphql.types.Artist;
import com.example.graphql.types.Show;
import com.netflix.graphql.dgs.*;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class ShowsDatafetcher {

    private final List<Show> shows = List.of(
            new Show("Stranger Things", 2016, new Artist("Arijit Singh", 40)),
            new Show("Ozark", 2017, Artist.newBuilder().age(39).name("Atif Alslam").build()),
            new Show("The Crown", 2016, Artist.newBuilder().age(33).name("Jubin Nautyal").build()),
            new Show("Dead to Me", 2019, Artist.newBuilder().name( "Sonu Nigam").age(44).build()),
            new Show("Orange is the New Black", 2013, Artist.newBuilder().name( "Sonu Nigam").age(44).build())
    );

    @DgsQuery
    public List<Show> shows(@InputArgument String titleFilter) {
        if(titleFilter == null) {
            return shows;
        }
        return shows.stream().filter(s -> s.getTitle().contains(titleFilter)).collect(Collectors.toList());
    }

    /**
     * Shows loading of child objects from parent query.
     * @param dgsDataFetchingEnvironment
     * @return
     */
    @DgsData(parentType = "Query")
    public List<Show> showsByArtist(DgsDataFetchingEnvironment dgsDataFetchingEnvironment) {
        String source = dgsDataFetchingEnvironment.getArgument("artistFilter");
        return shows.stream().filter(show -> show.getArtist().getName().equals(source)).collect(Collectors.toList());
    }
}
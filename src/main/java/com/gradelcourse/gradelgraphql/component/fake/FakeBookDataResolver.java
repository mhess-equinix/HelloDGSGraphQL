package com.gradelcourse.gradelgraphql.component.fake;

import com.gradelcourse.gradelgraphql.datasource.FakeBookDataSource;
import com.gradelcourse.gradelgraphql.generated.DgsConstants;
import com.gradelcourse.gradelgraphql.generated.types.Book;
import com.gradelcourse.gradelgraphql.generated.types.ReleaseHistory;
import com.gradelcourse.gradelgraphql.generated.types.ReleaseHistoryInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class FakeBookDataResolver {

    @DgsData(parentType = "Query", field = "books")
    public List<Book> booksWrittenBy(@InputArgument(name = "author") Optional<String> authorName) {
        if (authorName.isEmpty() || StringUtils.isBlank(authorName.get())) {
            return FakeBookDataSource.BOOK_LIST;
        }
        return FakeBookDataSource.BOOK_LIST.stream()
                .filter(book -> StringUtils.containsIgnoreCase(book.getAuthor().getName(), authorName.get()))
                .collect(Collectors.toList());
    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.BooksByReleased
    )
    public List<Book> getBooksByReleased(DataFetchingEnvironment dataFetchingEnvironment) {
        var releasedMap = (Map<String, Object>) dataFetchingEnvironment
                .getArgument(DgsConstants.QUERY.BOOKSBYRELEASED_INPUT_ARGUMENT.ReleasedInput);
        var releasedInput = ReleaseHistoryInput.newBuilder()
                .printedEdition((boolean) releasedMap.get(DgsConstants.RELEASEHISTORYINPUT.PrintedEdition))
                .year((int) releasedMap.get(DgsConstants.RELEASEHISTORYINPUT.Year)).build();

        return FakeBookDataSource.BOOK_LIST.stream()
                .filter(book -> this.matchReleaseHistory(releasedInput, book.getReleased()))
                .collect(Collectors.toList());
    }

    public boolean matchReleaseHistory(ReleaseHistoryInput input, ReleaseHistory element) {
        return input.getPrintedEdition().equals(element.getPrintedEdition()) && Objects.equals(input.getYear(), element.getYear());
    }

}

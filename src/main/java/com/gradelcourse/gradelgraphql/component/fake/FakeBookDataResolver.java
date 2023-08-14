package com.gradelcourse.gradelgraphql.component.fake;

import com.gradelcourse.gradelgraphql.datasource.FakeBookDataSource;
import com.gradelcourse.gradelgraphql.generated.types.Book;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
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
}

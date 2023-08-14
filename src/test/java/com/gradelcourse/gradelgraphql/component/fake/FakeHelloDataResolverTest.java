package com.gradelcourse.gradelgraphql.component.fake;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FakeHelloDataResolverTest {
  @Autowired DgsQueryExecutor dgsQueryExecutor;

  @Test
  void oneHelloGraphQL() {
    @Language("GraphQL")
    var graphQlQuery =
        """
    query HelloGraphQL {
        oneHello{
            text
            randomNumber
        }
    }
""";
    String text = dgsQueryExecutor.executeAndExtractJsonPath(graphQlQuery, "data.oneHello.text");
    Integer randomNumber =
        dgsQueryExecutor.executeAndExtractJsonPath(graphQlQuery, "data.oneHello.randomNumber");

    assertFalse(StringUtils.isBlank(text));
    assertNotNull(randomNumber);
  }

  @Test
  void allHellosGraphQL() {
    var graphQLQuery =
        """
    query allHellos{
    allHellos{
        text
        randomNumber
        }
    }
""";

    List<String> texts =
        dgsQueryExecutor.executeAndExtractJsonPath(graphQLQuery, "data.allHellos[*].text");
    List<Integer> randomNumbers =
        dgsQueryExecutor.executeAndExtractJsonPath(graphQLQuery, "data.allHellos[*].randomNumber");

    assertNotNull(texts);
    assertFalse(texts.isEmpty());
    assertNotNull(randomNumbers);
    assertFalse(randomNumbers.isEmpty());
    assertEquals(texts.size(), randomNumbers.size());
  }
}

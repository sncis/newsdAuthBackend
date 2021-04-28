package com.zewde.newsdAuthentication.unitTests.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.zewde.newsdAuthentication.entities.Article;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;

import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
public class ArticleJsonFilterTest {

private final static Logger logger = LoggerFactory.getLogger(ArticleJsonFilterTest.class);

  private JsonSchema getSchema(){
    InputStream schemaAsStream = ArticleJsonFilterTest.class.getClassLoader().getResourceAsStream("jsonSchema/article.schema.json");
    JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaAsStream);
    return schema;
  }

  private JsonNode createJsonNode(Article article) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(article);
    JsonNode jsonNode = mapper.readTree(json);
    return jsonNode;
  }

  @Test
  public void shouldValidateValidJson() throws JsonProcessingException {

    Article article = new Article("1","kitv.com","author","some title", "By Chris Isidore, CNN Business Elon..","http://www.kitv.com/story/42196143/to-cap-", "2020-06-01 16:11:00", "NA", "DE", "de", "1234","Copyright 2000 - 2020 WorldNow and KITV",true);

    System.out.println(article);
    JsonSchema schema = getSchema();
    JsonNode jsonNode = createJsonNode(article);
    System.out.println(jsonNode);

    logger.info(String.valueOf(jsonNode));
    Set<ValidationMessage> error = schema.validate(jsonNode);

    logger.info(String.valueOf(error));
    assertEquals(error.size(),0);
  }


  @Test
  public void shouldThrowValidationError_whenArticleIsInvalid() throws JsonProcessingException {

    Article article = new Article("1","wrongUrl","author","some title", "By Chris Isidore, CNN Business Elon..","www.kitv.com/story/42196143/to-cap-", "2020-06-01 00<>", "NA", "DE", "de", "1234","Copyright 2000 - 2020 WorldNow and KITV",true);

    JsonSchema schema = getSchema();
    JsonNode jsonNode = createJsonNode(article);

    logger.info(String.valueOf(jsonNode));
    Set<ValidationMessage> error = schema.validate(jsonNode);

    logger.info(String.valueOf(error));
    assertEquals(error.size(),3);
  }
}

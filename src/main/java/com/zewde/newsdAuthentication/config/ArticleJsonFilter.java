package com.zewde.newsdAuthentication.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class ArticleJsonFilter extends OncePerRequestFilter {
  private final static Logger logger = LoggerFactory.getLogger(ArticleJsonFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    System.out.println("################ Initialising Article Filter ##################");

    CachedHttpServletRequest cachedArticleRequest =  new CachedHttpServletRequest(httpServletRequest);

    if(httpServletRequest.getMethod().equalsIgnoreCase("POST")){
      logger.info("Validating data for request: {}", cachedArticleRequest.getRequestURI());
      logger.info("Request protocol from : {}", cachedArticleRequest.getProtocol());

      JsonSchema articleSchema = createJsonSchema();
      JsonNode articleJson = createJsonNode(cachedArticleRequest.getInputStream());

      Set<ValidationMessage> errors = articleSchema.validate(articleJson);
      StringBuilder errorOutput = new StringBuilder();

      for(ValidationMessage error : errors){
        logger.error("Article Validation Error : {}", error);
        errorOutput.append(error.toString()).append("\n");
      }

      if(errors.size() > 0)
      {
        logger.error("Errors in Article Data : \n {}", errorOutput);
        httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"Article Data are not valid! Article cannot be stored");
      }else{
        logger.info("Successfully validated article");
        System.out.println("################ End of Article Filter ##################");

        filterChain.doFilter(cachedArticleRequest, httpServletResponse);
      }
    }else{
      logger.info("No article to filter!");
      System.out.println("################ End of Article Filter ##################");

      filterChain.doFilter(cachedArticleRequest, httpServletResponse);
    }

  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request)
      throws ServletException {
    String path = request.getRequestURI();
    List<String> urls = Arrays.asList("/auth/register","/auth/login","/logout","/auth/resendConfirmationToken","/auth/confirm","/", "/error", "/favicon.ico", "/favicon");
    logger.info("Url is filtered" + urls.contains(path));

    return urls.contains(path);
  }

  private JsonSchema createJsonSchema(){
    InputStream jsonValidationSchemaAsStream = ArticleJsonFilter.class.getClassLoader().getResourceAsStream("jsonSchema/article.schema.json");
    JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(jsonValidationSchemaAsStream);
    return schema;
  }

  private JsonNode createJsonNode(ServletInputStream inputStream) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
    return objectMapper.readTree(inputStream);

  }
}

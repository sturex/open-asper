package dev.asper.app.config;

import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.parser.ParserOptions;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Configuration
public class GraphqlConfig {

    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer() {
        ParserOptions defaultParserOptions = ParserOptions.newParserOptions().maxTokens(90000).build();
        ParserOptions.setDefaultParserOptions(defaultParserOptions);
        ParserOptions.setDefaultOperationParserOptions(defaultParserOptions);
        return (wiringBuilder) -> wiringBuilder
                .scalar(unixTimestampScalar())
                .scalar(datetimeScalar());
    }

    private GraphQLScalarType datetimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("Timestamp")
                .coercing(new Coercing<Timestamp, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof Timestamp) {
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTimeInMillis((Long) dataFetcherResult);
                            return FORMATTER.format((Timestamp) dataFetcherResult);
                        }
                        throw CoercingSerializeException.newCoercingSerializeException().build();
                    }

                    @Override
                    public Timestamp parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof String) {
                            try {
                                return Timestamp.valueOf((String) input);
                            } catch (Exception e) {
                                throw CoercingSerializeException.newCoercingSerializeException().build();
                            }
                        }
                        throw CoercingSerializeException.newCoercingSerializeException().build();
                    }

                    @Override
                    public Timestamp parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof StringValue) {
                            try {
                                return Timestamp.valueOf(((StringValue) input).getValue());
                            } catch (Exception e) {
                                throw CoercingSerializeException.newCoercingSerializeException().build();
                            }
                        }
                        throw CoercingParseLiteralException.newCoercingParseLiteralException().build();
                    }
                })
                .build();
    }

    private GraphQLScalarType unixTimestampScalar() {
        return GraphQLScalarType.newScalar()
                .name("UnixTimestamp")
                .coercing(new Coercing<Long, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof Long) {
                            return Long.toString((Long) dataFetcherResult);
                        }
                        throw CoercingSerializeException.newCoercingSerializeException().build();
                    }

                    @Override
                    public Long parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof String) {
                            return Long.valueOf((String) input);
                        }
                        throw CoercingSerializeException.newCoercingSerializeException().build();
                    }

                    @Override
                    public Long parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof IntValue) {
                            return ((IntValue) input).getValue().longValue();
                        }
                        throw CoercingParseLiteralException.newCoercingParseLiteralException().build();
                    }
                })
                .build();
    }
}

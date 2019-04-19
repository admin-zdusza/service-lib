package pl.zdusza.validators;

import io.vertx.ext.web.RoutingContext;
import pl.zdusza.Statuses;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public final class EnumValidator {

    private EnumValidator() {
    }

    public static <T extends Enum<T>> Optional<T> validatePathParam(final RoutingContext context,
                                                                    final String pathParam,
                                                                    final T[] possibleValues,
                                                                    final Class<T> clazz) {
        final String value = context.request().getParam(pathParam);
        final Optional<T> result = EnumParser.parse(value, clazz);
        final Set<T> possibleValuesSet = new LinkedHashSet<>(Arrays.asList(possibleValues));
        if (!result.isPresent() || !possibleValuesSet.contains(result.get())) {
            context.response().putHeader("content-type", "application/plain; charset=utf-8");
            context.response().setStatusCode(Statuses.BAD_REQUEST)
                    .end("Wrong path param " + pathParam + ": " + value
                            + ". Possible values are: "
                            + EnumParser.format(possibleValues));
        }
        return result;
    }

    public static <T extends Enum<T>> Optional<T> validateHeader(final RoutingContext context,
                                                                 final String header,
                                                                 final T[] possibleValues,
                                                                 final Class<T> clazz) {
        final Optional<String> value = Optional.ofNullable(context.request().getHeader(header));
        if (value.isPresent()) {
            final Optional<T> result = EnumParser.parse(value.get(), clazz);
            final Set<T> possibleValuesSet = new LinkedHashSet<>(Arrays.asList(possibleValues));
            if (!result.isPresent() || !possibleValuesSet.contains(result.get())) {
                context.response().putHeader("content-type", "application/plain; charset=utf-8");
                context.response().setStatusCode(Statuses.BAD_REQUEST)
                        .end("Wrong header " + header + ": " + value
                                + ". Possible values are: "
                                + EnumParser.format(possibleValues));
            }
            return result;
        }
        context.response().putHeader("content-type", "application/plain; charset=utf-8");
        context.response().setStatusCode(Statuses.BAD_REQUEST)
                .end("Empty header " + header
                        + ". Possible values are: "
                        + EnumParser.format(possibleValues));
        return Optional.empty();
    }
}

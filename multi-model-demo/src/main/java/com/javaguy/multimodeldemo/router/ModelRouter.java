package com.javaguy.multimodeldemo.router;

import com.javaguy.multimodeldemo.model.ModelRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

//helps us define which modelrole handles which task
@Component
public class ModelRouter {
    private static final List<String> CODE_KEYWORDS = List.of(
            "code", "function", "class", "method", "bug", "error",
            "exception", "java", "spring", "python", "kotlin", "sql",
            "algorithm", "refactor", "implement", "compile", "stacktrace"
    );

    private static final List<String> REASONING_KEYWORDS = List.of(
            "why", "compare", "trade-off", "tradeoff", "pros and cons",
            "analyze", "explain the difference", "which is better",
            "should i", "strategy", "decide"
    );

    public ModelRole classify(String question){
        //normalize the user question
        String normalizedQuestion = question.toLowerCase(Locale.ROOT);

        //check if the  keyword
        if (containsAny(normalizedQuestion, CODE_KEYWORDS)){
            return ModelRole.CODE;
        }
        if (containsAny(normalizedQuestion, REASONING_KEYWORDS)){
            return ModelRole.REASONING;
        }

        return ModelRole.FAST;
    }

    public boolean containsAny(String text, List<String> keyWords){
        return keyWords.stream().anyMatch(text::contains);   }
}

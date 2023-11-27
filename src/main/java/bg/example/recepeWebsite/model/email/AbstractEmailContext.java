package bg.example.recepeWebsite.model.email;

import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * All email related information will be passed through this instance.
 */
public abstract class AbstractEmailContext {

    private String from;
    private String to;
    private String subject;
    private String templateLocation;
    private Map<String, Object> variables;
    private Locale locale;

    public AbstractEmailContext() {
        this.variables = new HashMap<>();
    }

    public String getFrom() {
        return from;
    }

    public AbstractEmailContext setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public AbstractEmailContext setTo(String to) {
        this.to = to;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public AbstractEmailContext setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getTemplateLocation() {
        return templateLocation;
    }

    public AbstractEmailContext setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
        return this;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public AbstractEmailContext setVariables(Map<String, Object> variables) {
        this.variables = variables;
        return this;
    }

    public Locale getLocale() {
        return locale;
    }

    public AbstractEmailContext setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public <T> void initContext(T context) {
        //we can do any common configuration setup here
        // like setting up some base URL and context
    }

    public Object put(String key, Object value) {
        return key == null ? null : this.variables.put(key.intern(), value);
    }

    public Context getContext() {
        Context ctx = new Context();
        ctx.setLocale(locale);
        ctx.setVariables(variables);
        return ctx;
    }
}
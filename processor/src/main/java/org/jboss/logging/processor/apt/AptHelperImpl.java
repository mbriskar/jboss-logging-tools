package org.jboss.logging.processor.apt;

import org.jboss.logging.LogMessage;
import org.jboss.logging.Logger;
import org.jboss.logging.Message;
import org.jboss.logging.MessageBundle;
import org.jboss.logging.MessageLogger;
import org.jboss.logging.generator.Annotations.FormatType;
import org.jboss.logging.generator.apt.AptHelper;
import org.jboss.logging.processor.BaseAnnotations;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Date: 24.08.2011
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class AptHelperImpl implements AptHelper {

    @Override
    public FormatType messageFormat(final ExecutableElement method) {
        FormatType result = null;
        final Message message = method.getAnnotation(BaseAnnotations.MESSAGE_ANNOTATION);
        if (message != null) {
            switch (message.format()) {
                case MESSAGE_FORMAT:
                    result = FormatType.MESSAGE_FORMAT;
                    break;
                case PRINTF:
                    result = FormatType.PRINTF;
                    break;
            }
        }
        return result;
    }

    @Override
    public String projectCode(final TypeElement intf) {
        String result = null;
        final MessageBundle bundle = intf.getAnnotation(BaseAnnotations.MESSAGE_BUNDLE_ANNOTATION);
        final MessageLogger logger = intf.getAnnotation(BaseAnnotations.MESSAGE_LOGGER_ANNOTATION);
        if (bundle != null) {
            result = bundle.projectCode();
        } else if (logger != null) {
            result = logger.projectCode();
        }
        return result;
    }

    @Override
    public boolean hasMessageId(final ExecutableElement method) {
        final Message message = method.getAnnotation(BaseAnnotations.MESSAGE_ANNOTATION);
        return (message != null && (message.id() != Message.NONE && message.id() != Message.INHERIT));
    }

    @Override
    public boolean inheritsMessageId(final ExecutableElement method) {
        final Message message = method.getAnnotation(BaseAnnotations.MESSAGE_ANNOTATION);
        return (message != null && (message.id() == Message.INHERIT));
    }

    @Override
    public int messageId(final ExecutableElement method) {
        final Message message = method.getAnnotation(BaseAnnotations.MESSAGE_ANNOTATION);
        return (message == null ? Message.NONE : message.id());
    }

    @Override
    public String messageValue(final ExecutableElement method) {
        final Message message = method.getAnnotation(BaseAnnotations.MESSAGE_ANNOTATION);
        return (message == null ? null : message.value());
    }

    @Override
    public String loggerMethod(final FormatType formatType) {
        return "log" + (formatType == null ? "" : formatType.logType());
    }

    @Override
    public String logLevel(final ExecutableElement method) {
        String result = null;
        final LogMessage logMessage = method.getAnnotation(BaseAnnotations.LOG_MESSAGE_ANNOTATION);
        if (logMessage != null) {
            final Logger.Level logLevel = (logMessage.level() == null ? Logger.Level.INFO : logMessage.level());
            result = String.format("%s.%s.%s", Logger.class.getSimpleName(), Logger.Level.class.getSimpleName(), logLevel.name());
        }
        return result;
    }
}
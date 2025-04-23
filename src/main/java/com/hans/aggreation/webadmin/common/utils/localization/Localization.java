package com.hans.aggreation.webadmin.common.utils.localization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Multiple language utils
 */
@Component
public class Localization {

    private static MessageSource messageSource;

    /**
     * Inject messageSource bean
     *
     * @param messageSource
     */
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        Localization.messageSource = messageSource;
    }


    public static String getLocalizedString(String key) {
        Locale locale = LocaleContextHolder.getLocale(); // 获取当前线程的 Locale
        return getLocalizedString(key, null, locale);
    }

    /**
     *
     * @param key
     * @param arguments
     * @return
     */
    public static String getLocalizedString(String key, Object[] arguments) {
        Locale locale = LocaleContextHolder.getLocale(); // 获取当前线程的 Locale
        return getLocalizedString(key, arguments, locale);
    }
    public static String getLocalizedString(String key, List arguments) {
        Locale locale = LocaleContextHolder.getLocale(); // 获取当前线程的 Locale
        return getLocalizedString(key, arguments.toArray(), locale);
    }
    public static String getLocalizedString(String key, Object[] arguments, Locale locale) {
        try {
            if (locale==null) {
                locale = Locale.ENGLISH;
            }
            return messageSource.getMessage(key, arguments, locale);
        } catch (NoSuchMessageException e) {
            return "???" + key + "???"; // 如果找不到对应的键，返回占位符
        }
    }
}

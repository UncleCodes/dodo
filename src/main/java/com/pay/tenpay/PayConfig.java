package com.pay.tenpay;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.pay.tenpay.TenpayAccount.AccountType;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Configuration
@PropertySource("classpath:/tenpay_config.properties")
public class PayConfig {

    private static final Logger               LOGGER                  = LoggerFactory.getLogger(PayConfig.class);

    @Autowired
    private Environment                       env;

    public static final String                TENPAY_ACCOUNTS_KEY     = "dodo.pay.tenpay.accounts";
    public static final String                TENPAY_APP_ID_KEY       = "dodo.pay.tenpay.{0}.APP_ID";
    public static final String                TENPAY_PARTNER_KEY      = "dodo.pay.tenpay.{0}.PARTNER";
    public static final String                TENPAY_PARTNER_KEY_KEY  = "dodo.pay.tenpay.{0}.PARTNER_KEY";
    public static final String                TENPAY_NOTIFY_URL_KEY   = "dodo.pay.tenpay.{0}.NOTIFY_URL";
    public static final String                TENPAY_ACCOUNT_TYPE_KEY = "dodo.pay.tenpay.{0}.ACCOUNT_TYPE";

    private static Map<String, TenpayAccount> tenpayAccounts          = new HashMap<String, TenpayAccount>();

    @PostConstruct
    public void init() {
        String[] accounts = env.getProperty(TENPAY_ACCOUNTS_KEY).split(",");
        for (int i = 0; i < accounts.length; i++) {
            TenpayAccount account = new TenpayAccount();
            account.setName(accounts[i]);
            account.setAppId(env.getProperty(MessageFormat.format(TENPAY_APP_ID_KEY, account.getName())));
            account.setNotifyUrl(env.getProperty(MessageFormat.format(TENPAY_NOTIFY_URL_KEY, account.getName())));
            account.setPartner(env.getProperty(MessageFormat.format(TENPAY_PARTNER_KEY, account.getName())));
            account.setPartnerKey(env.getProperty(MessageFormat.format(TENPAY_PARTNER_KEY_KEY, account.getName())));
            account.setType(AccountType.valueOf(env.getProperty(MessageFormat.format(TENPAY_ACCOUNT_TYPE_KEY,
                    account.getName()))));
            tenpayAccounts.put(account.getName(), account);
        }
        LOGGER.info("**********TenpayAccount****************");
        LOGGER.info(tenpayAccounts.toString());
        LOGGER.info("**********TenpayAccount****************");
    }

    public static TenpayAccount getTenpayAccount(String accountName) {
        return tenpayAccounts.get(accountName);
    }
}

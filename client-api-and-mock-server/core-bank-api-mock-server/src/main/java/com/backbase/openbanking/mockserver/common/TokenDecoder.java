package com.backbase.openbanking.mockserver.common;

import com.backbase.openbanking.mockserver.common.exceptions.MockDataException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;

@Log4j2
@Data
public class TokenDecoder {

    public String TokenId;
    public String CWUser;
    public String exp;

    public static TokenDecoder getDecoded(String encodedToken) {
        try {
            String[] pieces = encodedToken.split("\\.");
            String b64payload = pieces[1];
            String jsonString = new String(Base64.decodeBase64(b64payload), "UTF-8");
            SimpleObjectMapper mapper = new SimpleObjectMapper();
            return mapper.read(jsonString, TokenDecoder.class);
        } catch (Exception ex) {
            log.error("Error decoding token", ex);
            throw new MockDataException("Cannot decode " + encodedToken, ex);
        }
    }
}

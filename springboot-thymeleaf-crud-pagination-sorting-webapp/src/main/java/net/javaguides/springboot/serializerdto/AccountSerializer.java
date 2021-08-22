package net.javaguides.springboot.serializerdto;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import net.javaguides.springboot.model.Account;
public class AccountSerializer extends StdSerializer<Account>{
	public AccountSerializer() {
	    this(null);
	    }

	    protected AccountSerializer(Class<Account>
	        t) {
	        super(t);
	        }
	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(Account value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();

        provider.defaultSerializeField("id", value.getId(), gen);
        provider.defaultSerializeField("username", value.getUsername(), gen);
        provider.defaultSerializeField("email", value.getEmail(), gen);
        
        provider.defaultSerializeField("enabled", value.getEnabled(), gen);
        provider.defaultSerializeField("locked", value.getLocked(), gen);
        provider.defaultSerializeField("likedRoles", value.getLikedRoles(), gen);
        
        gen.writeEndObject();
	}

}

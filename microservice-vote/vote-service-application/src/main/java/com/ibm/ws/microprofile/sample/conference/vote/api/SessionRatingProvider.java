/*
 * (C) Copyright IBM Corporation 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.ws.microprofile.sample.conference.vote.api;

import static com.ibm.ws.microprofile.sample.conference.vote.utils.Debug.isDebugEnabled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionRatingProvider implements MessageBodyReader<SessionRating>, MessageBodyWriter<SessionRating> {

	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("SRP.isReadable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(SessionRating.class));
		return clazz.equals(SessionRating.class);
	}

	@Override
	public SessionRating readFrom(Class<SessionRating> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream is) throws IOException, WebApplicationException {
		JsonReader rdr = null; 
		try {
		     rdr = Json.createReader(is);
		     JsonObject sessionRatingJson = rdr.readObject();
		     if (isDebugEnabled()) System.out.println(sessionRatingJson);
		     JsonString idJson = sessionRatingJson.getJsonString("id");
		     JsonString sessionJson = sessionRatingJson.getJsonString("session");
		     JsonString attendeeIdJson = sessionRatingJson.getJsonString("attendeeId");
		     int rating = sessionRatingJson.getInt("rating");
		     SessionRating sessionRating;
		     if (idJson != null) {
		    	 sessionRating = new SessionRating(idJson.getString(), sessionJson.getString(), attendeeIdJson.getString(), rating);
		     } else {
		    	 sessionRating = new SessionRating(sessionJson.getString(), attendeeIdJson.getString(), rating);
		     }
		     return sessionRating;
		} finally {
			if (rdr != null) {
				rdr.close();
			}
		}
		
	}
	
	@Override
	public long getSize(SessionRating sessionRating, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("SRP.getSize() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType);
		return 0;
	}

	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("SRP.isWriteable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(SessionRating.class));
		return clazz.equals(SessionRating.class);
	}

	@Override
	public void writeTo(SessionRating sessionRating, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> map, OutputStream os) throws IOException, WebApplicationException {
		JsonGenerator jsonGenerator = Json.createGenerator(os);
		jsonGenerator.writeStartObject();
		jsonGenerator.write("id", sessionRating.getId());
		jsonGenerator.write("session", sessionRating.getSession());
		jsonGenerator.write("attendeeId", sessionRating.getAttendeeId());
		jsonGenerator.write("rating", sessionRating.getRating());
		jsonGenerator.writeEnd();
		jsonGenerator.close();
	}
}

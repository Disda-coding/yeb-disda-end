package com.disda.cowork.config;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义Authority解析器(反序列化)
 *
 */
public class CustomAuthorityDeserializer extends JsonDeserializer {

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        //codec 编解码器
        ObjectCodec codec = jsonParser.getCodec();
        ObjectMapper mapper = (ObjectMapper)codec;
        //用mapper读取解析对象
        TreeNode node = mapper.readTree(jsonParser);
        JsonNode jsonNode = (JsonNode)node;

        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();

        //获取对应元素，返回一个迭代器
        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()){
            JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
        }
        return grantedAuthorities;
    }
}

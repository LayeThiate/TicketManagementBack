package fr.urss.common.api.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

public class MergePatchMapper extends ObjectMapper {

    public <T> T mergePatch(JsonMergePatch mergePatch, T toPatch, Class<T> c) {
        var toPatchValue = convertValue(toPatch, JsonNode.class);
        try {
            var patched = mergePatch.apply(toPatchValue);
            return convertValue(patched, c);
        } catch (JsonPatchException ignored) {
            return toPatch;
        }
    }
}

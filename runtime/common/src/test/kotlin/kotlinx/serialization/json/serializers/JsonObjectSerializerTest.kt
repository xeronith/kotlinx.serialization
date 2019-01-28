/*
 * Copyright 2017-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.serialization.json.serializers

import kotlinx.serialization.json.*
import kotlin.test.*

class JsonObjectSerializerTest : JsonTestBase() {

    private val expected = "{element:{literal:1,nullKey:null,nested:{\"another literal\":\"some value\"}}}"
    private val expectedTopLevel = "{literal:1,nullKey:null,nested:{\"another literal\":\"some value\"}}"

    @Test
    fun testJsonObject() = parametrizedTest { useStreaming ->
        val wrapper = JsonObjectWrapper(prebuiltJson())
        val string = unquoted.stringify(wrapper, useStreaming)
        assertEquals(expected, string)
        assertEquals(wrapper, unquoted.parse(string, useStreaming))
    }

    @Test
    fun testJsonObjectAsElement() = parametrizedTest { useStreaming ->
        val wrapper = JsonElementWrapper(prebuiltJson())
        val string = unquoted.stringify(wrapper, useStreaming)
        assertEquals(expected, string)
        assertEquals(wrapper, unquoted.parse(string, useStreaming))
    }

    @Test // TODO Top-level nulls are not supported in tagged encoders
    fun testTopLevelJsonObject() { // parametrizedTest { useStreaming ->
        val string = unquoted.stringify(JsonObjectSerializer, prebuiltJson())
        assertEquals(expectedTopLevel, string)
        assertEquals(prebuiltJson(), unquoted.parse(JsonObjectSerializer, string))
    }

    @Test // TODO Top-level nulls are not supported in tagged encoders
    fun testTopLevelJsonObjectAsElement() {
        val string = unquoted.stringify(JsonElementSerializer, prebuiltJson())
        assertEquals(expectedTopLevel, string)
        assertEquals(prebuiltJson(), unquoted.parse(JsonElementSerializer, string))
    }

    @Test
    fun testJsonObjectToString() {
        val prebuiltJson = prebuiltJson()
        val string = nonStrict.stringify(JsonElementSerializer, prebuiltJson)
        assertEquals(string, prebuiltJson.toString())
    }

    @Test
    fun testDocumentationSample() {
        val string = Json.stringify(JsonElementSerializer, json { "key" to 1.0 })
        val literal = Json.parse(JsonElementSerializer, string)
        assertEquals(JsonObject(mapOf("key" to JsonLiteral(1.0))), literal)
    }

    private fun prebuiltJson(): JsonObject {
        return json {
            "literal" to 1
            content["nullKey"] = JsonNull
            "nested" to json {
                "another literal" to "some value"
            }
        }
    }
}

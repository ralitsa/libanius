/*
 * Copyright 2012 James McCabe <james@oranda.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oranda.libanius.model.wordmapping

import org.specs2.mutable.Specification
import com.oranda.libanius.Props

class WordMappingGroupSpec extends Specification {

  "a word-mapping group" should {
    
    val wmgCustomFormat =
        "wordMappingGroup keyType=\"English word\" valueType=\"German word\"\n" +
        "against|wider\n" +
        "entertain|unterhalten\n" +
        "teach|unterrichten\n" +
        "winner|Siegerin\n" +
        "en route|unterwegs\n" +
        "full|satt/voll\n" +
        "interrupted|unterbrochen\n" +
        "contract|Vertrag\n" +
        "rides|reitet\n" +
        "sweeps|streicht"
        
    val wmgXml = 
  <wordMappingGroup keyType="English word" valueType="German word">
    <wordMapping key="against">  
      <wordMappingValue value="wider"><userAnswers></userAnswers></wordMappingValue> 
    </wordMapping>
    <wordMapping key="entertain">  
      <wordMappingValue value="unterhalten"><userAnswers></userAnswers></wordMappingValue> 
    </wordMapping>
    <wordMapping key="teach">
      <wordMappingValue value="unterrichten"><userAnswers></userAnswers></wordMappingValue> 
    </wordMapping>
    <wordMapping key="winner"> 
      <wordMappingValue value="Siegerin"><userAnswers></userAnswers></wordMappingValue> 
    </wordMapping>
    <wordMapping key="en route"> 
      <wordMappingValue value="unterwegs"><userAnswers></userAnswers></wordMappingValue> 
    </wordMapping>
    <wordMapping key="full">
      <wordMappingValue value="satt"><userAnswers></userAnswers></wordMappingValue> 
      <wordMappingValue value="voll"><userAnswers></userAnswers></wordMappingValue>   
    </wordMapping>
    <wordMapping key="interrupted"> 
      <wordMappingValue value="unterbrochen"><userAnswers></userAnswers></wordMappingValue> 
    </wordMapping>
    <wordMapping key="contract">
      <wordMappingValue value="Vertrag"><userAnswers></userAnswers></wordMappingValue>        
    </wordMapping>
    <wordMapping key="rides"> 
      <wordMappingValue value="reitet"><userAnswers></userAnswers></wordMappingValue> 
    </wordMapping>
    <wordMapping key="sweeps">  
      <wordMappingValue value="streicht"><userAnswers></userAnswers></wordMappingValue> 
    </wordMapping>
  </wordMappingGroup>
  
    Props.ANDROID = false
    
    val wmg = WordMappingGroup.fromCustomFormat(wmgCustomFormat)
    val wmgFromXml = WordMappingGroup.fromXML(wmgXml)
    
    "be parseable from custom format" in {
      wmg.keyType mustEqual "English word"
      wmg.valueType mustEqual "German word"
      wmg.toCustomFormat(new StringBuilder()).toString mustEqual wmgCustomFormat
      wmg.numKeyWords mustEqual 10
    }
    
    "be parseable from XML" in {
      wmgFromXml.keyType mustEqual "English word"
      wmgFromXml.valueType mustEqual "German word"
      wmgFromXml.numKeyWords mustEqual 10
    }
    
    "accept the addition of a new word-mapping" in {
      val wmgLocal = WordMappingGroup.fromCustomFormat(wmgCustomFormat)
      wmgLocal.contains("good") mustEqual false
      wmgLocal.addWordMapping("good", "gut")
      wmgLocal.contains("good") mustEqual true
    }
    
    "accept new values for an existing word-mapping" in {
      val wmgLocal = WordMappingGroup.fromCustomFormat(wmgCustomFormat)
      val valuesForAgainst = wmgLocal.findValuesFor("against")
      valuesForAgainst.isDefined mustEqual true
      valuesForAgainst.get.size mustEqual 1
      wmgLocal.addWordMapping("against", "gegen")
      wmgLocal.findValuesFor("against").get.size mustEqual 2
    }
    
    "generate false answers similar to a correct answer" in {
      val wmvs = new WordMappingValueSet
      wmvs.addValue(new WordMappingValue("unterhalten"))
      val falseAnswers = wmg.makeFalseSimilarAnswers(
          wordMappingCorrectValues = wmvs,
          correctValue = new WordMappingValue("unterhalten"), 
          numCorrectAnswersSoFar = 2, numFalseAnswersRequired = 5)
      falseAnswers.contains("unterrichten") mustEqual true
    }
  }
}
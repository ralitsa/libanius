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

import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable
import scala.xml.Unparsed
import scala.xml.Text
import scala.util.Random
import android.text.TextUtils
import android.util.Log
import com.oranda.libanius.util.StringUtil
import com.sun.xml.internal.ws.util.StringUtils
import com.oranda.libanius.util.Platform
import com.oranda.libanius.model.ModelComponent
import scala.collection.mutable.ArrayBuffer

case class WordMappingValueSet() extends ModelComponent {
    
  // A List is a bit faster than a Set when deserializing. High performance is required.
  var values = List[WordMappingValue]()
  
  override def toString = values.toString
  
  def toXML = values map (_.toXML)
    
  // Example: contract:696,697;698/treaty:796;798
  def toCustomFormat(strBuilder: StringBuilder) = 
    StringUtil.mkString(strBuilder, values, wmvToCustomFormat, '/')
  
  def wmvToCustomFormat(strBuilder: StringBuilder, wmv: WordMappingValue) =
    wmv.toCustomFormat(strBuilder)
    
  def strings: Iterable[String] = values map (_.toString ) 
  
  def size = values.size
  
  def numItemsAndCorrectAnswers = Pair(size, numCorrectAnswers)
  
  def numCorrectAnswers = {
    /*
     * This functional version is about twice as slow as the version actually used:
     * 
     * values.iterator.foldLeft(0)(_ + _.numCorrectAnswersInARow)
     */
    var numCorrectAnswers = 0        
    values.foreach { wmv => numCorrectAnswers += wmv.numCorrectAnswersInARow }
    numCorrectAnswers
  }
  
  def addValue(wordMappingValue: WordMappingValue) {
    if (!values.contains(wordMappingValue))
      values ::= wordMappingValue
  } 
  
  def filterOut(value: String) = { 
    values = values.filterNot(_.value == value)
    this
  }
  
  def removeValue(wordMappingValue: WordMappingValue): Boolean = {
    val existed = values.contains(wordMappingValue)
    values = values.filterNot(_ == wordMappingValue) // values -= wordMappingValue    
    existed
  } 
  
  def findPresentableWordMappingValue(currentPromptNumber: Int): 
      Option[WordMappingValue] = 
    values.iterator.find(_.isPresentable(currentPromptNumber))
  
  def findAnyUnfinishedWordMappingValue: Option[WordMappingValue] = 
    values.iterator.find(_.isUnfinished)    
    
  def findRandomWordValue(): String = {
    val randomIndex = Random.nextInt(values.size)
    val valueArray: Array[WordMappingValue] = values.toArray[WordMappingValue]
    valueArray(randomIndex).value
  }
  
  def findValue(value: String): Option[WordMappingValue] = values.find(_.value == value)
  
  def containsValue(value: String): Boolean = findValue(value).isDefined
  
  def valueBeginningWith(valueStart: String) = 
    values.find(_.value.startsWith(valueStart))
}


object WordMappingValueSet extends Platform {
  
  def combineValueSets(valueSets: Iterable[WordMappingValueSet]) =  
    valueSets.foldLeft(new ArrayBuffer[WordMappingValue]()) {          
        (acc, wm) => acc ++ wm.values/*AsScala*/
    }
  
  val wmvSplitter = getSplitter('/')
  // Example: contract:696,697;698/treaty:796;798
  def fromCustomFormat(str: String): WordMappingValueSet =
    new WordMappingValueSet() {
      wmvSplitter.setString(str)
      while (wmvSplitter.hasNext)
        addValue(WordMappingValue.fromCustomFormat(wmvSplitter.next))
      values = values.reverse
    }
    
  def fromXML(node: xml.Node): WordMappingValueSet = {
	new WordMappingValueSet() {
	  for (wordMappingValueXml <- node \\ "wordMappingValue")
	    addValue(WordMappingValue.fromXML(wordMappingValueXml))
	}
  }
}
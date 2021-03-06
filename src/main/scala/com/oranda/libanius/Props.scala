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

package com.oranda.libanius

object Props {
  
  var ANDROID = true
  
  val fileVocab = "vocabGer10k.txt"
  
  var fileQuizRoot = "quizGer10k"  // "quizTestData"
  
  val fileQuiz = fileQuizRoot + ".qui"
  
  val fileQuizLastBackup = fileQuizRoot + "Backup" + ".qui"
  
  val fileDictionary = "dictccGerEng.dct"
  
  val resQuizPublic = R.raw.quizger10kpublicqui
  
  val resDictPublic = R.raw.quizger10kpublicdct
  
  val NUM_CORRECT_ANSWERS_REQUIRED = 4 // 5
  
  // Variables passed between activities
  val KEY_WORD = "keyWord"
  val VALUE = "value"
  
}
package com.lakue.newsreader.Base

import android.util.Log

object ExtractKeyWord{

    fun getKeyWords(description: String): ArrayList<String> {
        val removeSpecialCharacters: String =
            SpecialCharactersRreplace(description)
        var feedKeyword: ArrayList<String> = ArrayList()
        //띄어쓰기마다 split
        val keywords: ArrayList<String> =
            removeSpecialCharacters.split(" ") as ArrayList<String>
        val keywordMap: HashMap<String, Int> = HashMap()

        var isKeyWordEquare = false
        for (keyword in keywords) {
            var trimKeyword = keyword.trim()
            //키워드 글자수가 2글자 이상인지 판단
            if (trimKeyword.length < 2)
                continue

            //처음 해쉬맵안에 데이터가 비어있으면 키워드(key)와 갯수(value:1)지정
            if (keywordMap.isEmpty()) {
                keywordMap.set(trimKeyword, 1)
            } else {
                //해쉬맵 안에 있는 키워드일 경우 value+1을 해주고,
                //없을 경우 키워드(key)와 갯수(value:1)지정
                for (key in keywordMap) {
                    if (key.key == trimKeyword) {
                        var count = keywordMap.get(trimKeyword)
                        count = count?.plus(1)
                        keywordMap.set(trimKeyword, count!!)
                        isKeyWordEquare = true
                        break
                    } else {
                        isKeyWordEquare = false
                    }
                }
                if (!isKeyWordEquare) {
                    keywordMap.set(trimKeyword, 1)
                }
            }
        }

        //받아온 키워드를 키값으로 오름차순 정렬(숫자,영어,ㄱ,ㄴ,ㄷ순)
        val result = keywordMap.toList().sortedBy { (key, _) -> key }.toMap()

        //정렬한 키워드에서 다시 갯수대로 내림차순 정렬(가장 많이 사용한 키워드가 맨 앞으로 오도록)
        val result1 = result.toList().sortedByDescending { (_, value) -> value }.toMap()

        //결과 = 가장 많이 사용된 키워드 -> 숫자 -> 영어 > 한글순..
        Log.e("sortKeyword", result1.toString())
        feedKeyword = ArrayList(result1.keys)

        return feedKeyword
    }

    //특수문자, 개행 제거
    fun SpecialCharactersRreplace(str: String): String {
        var str = str
        val match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]"
        str = str.replace(match.toRegex(), "")
        str = str.replace("\n", "")
        str = str.replace("\t", " ")
        return str
    }
}

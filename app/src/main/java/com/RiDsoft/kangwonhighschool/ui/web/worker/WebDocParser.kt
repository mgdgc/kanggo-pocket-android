package com.RiDsoft.kangwonhighschool.ui.web.worker

import android.util.Log

import net.htmlparser.jericho.Element
import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.Source

import java.io.IOException
import java.net.URL
import java.util.ArrayList

/**
 * Created by RiD on 2017. 7. 10..
 */

class WebDocParser {

    var title: String? = null
        private set
    var content: String? = null
        private set
    var writer: String? = null
        private set
    var date: String? = null
        private set
    private var mAttach: MutableList<String>? = null
    private var mAttachLink: MutableList<String>? = null

    val attachment: List<String>?
        get() = mAttach

    val attachLink: List<String>?
        get() = mAttachLink

    fun doParse(html: String): Int {

        mSource = Source(html)

        if (mSource != null) {
            mSource!!.fullSequentialParse()

            if (mSource!!.allElements.size != 0) {
                val article = mSource!!.getAllElements(HTMLElementName.ARTICLE)[0]

                val ul = article.getAllElements(HTMLElementName.UL)[0].childElements

                writer = (ul[0] as Element).content.toString().replace("<strong>작성자</strong>", "")
                date = (ul[1] as Element).content.toString().replace("<strong>작성일</strong>", "")
                title = article.getAllElements(HTMLElementName.H1)[0].content.toString().replace("<strong>제목</strong>", "")
                val content = article.getAllElements(HTMLElementName.DIV)[0]
                this.content = content.toString()

                val div = article.getAllElements(HTMLElementName.DIV)

                if (div != null) {

                    val dl = (div[1] as Element).getAllElements(HTMLElementName.DL)

                    if (dl != null) {

                        val files = (dl[0] as Element).getAllElements(HTMLElementName.DD)[0].getAllElements(HTMLElementName.A)

                        mAttach = ArrayList()
                        mAttachLink = ArrayList()

                        for (i in files.indices) {

                            val name = (files[i] as Element).getAttributeValue("title")

                            if (name != null) {
                                if (!name.contains("미리보기")) {
                                    mAttach!!.add((files[i] as Element).content.toString())
                                    mAttachLink!!.add("http://kanggo.net" + (files[i] as Element).getAttributeValue("href"))
                                }
                            } else {
                                mAttach!!.add((files[i] as Element).content.toString())
                                mAttachLink!!.add("http://kanggo.net" + (files[i] as Element).getAttributeValue("href"))
                            }
                        }
                        return 0

                    } else {
                        return 1
                    }

                } else {
                    return 1
                }

            } else {
                return 1
            }

        } else {
            return 1
        }
    }

    companion object {

        private var mSource: Source? = null
    }
}

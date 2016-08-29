package geb.util

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static spock.util.matcher.HamcrestSupport.*
import geb.Browser
import geb.spock.GebSpec

import org.apache.poi.ss.usermodel.*
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

class KDTcore extends GebSpec{
	static final String INPUT = "入力"
	static final String CLICK = "クリック"
	static final String ASSERT = "検証"
	static final String REPORT = "記録"
	static final String SLEEP = "停止"
	static final String EQUALE = "と等しい"
	static final String CONTAIN = "を含む"
	static final String NOTNULL = "存在する"
	static final String NULL = "存在しない"
	static final String NUMBER = "要素数"

	def static void kdtByExcel(url, Sheet sheet){
		Browser.drive {
			go url

			for(int i=2; sheet["A"+i].value != null; i++){
				String keyword      = sheet["A"+i].value
				String cssSelector  = sheet["B"+i].value
				String indexOrRange = sheet["C"+i].value
				String textContains = sheet["D"+i].value
				String cellValue    = sheet["E"+i].value
				String assertOption = sheet["F"+i].value
				if(indexOrRange == null){
					indexOrRange = 0
				}
				if(textContains == null){
					textContains = ""
				}

//				waitFor{ $(cssSelector, indexOrRange.toInteger(), text: contains(textContains)).isDisplayed() }

				switch(keyword){
					case INPUT:
						println i + "行目:" + "INPUT:"
						$(cssSelector, indexOrRange.toInteger(), text: contains(textContains)).value(cellValue)
						break
					case CLICK:
						println i + "行目:" + "CLICK"
						// $(cssSelector, indexOrRange.toInteger(), text: contains(textContains)).click()でもよいが、その場合、クリックメソッドがない要素をクリックできない
						def actions = new Actions(driver)
						WebElement item = $(cssSelector, indexOrRange.toInteger(), text: contains(textContains)).firstElement()
						actions.click(item).perform()
						break
					case ASSERT:
					expect:
						def actual = $(cssSelector, indexOrRange.toInteger(), text: contains(textContains))
						if(assertOption == NUMBER){
							actual = $(cssSelector, text: contains(textContains))
						}
						try{
							assertWithOption(i, actual, cellValue, assertOption)
						}catch(AssertionError e){
							report "error"
							throw e
						}
						break
					case REPORT:
						println i + "行目:" + "REPORT"
						report cellValue
						break
					case SLEEP:
						println i + "行目:" + "SLEEP"
						sleep cellValue.toInteger()
						break
					default:
						throw new IllegalArgumentException("無効なキーワードです")
				}
			}
		}
	}

	private static void assertWithOption(i, actual, cellValue, assertOption){
		switch(assertOption){
			case EQUALE:
				println i + "行目:" + "ASSERT EQUALE"
				assert that(actual.text(), is(cellValue))
				break
			case CONTAIN:
				println i + "行目:" + "ASSERT CONTAIN"
				assert that(actual.text(), containsString(cellValue))
				break
			case NOTNULL:
				println i + "行目:" + "ASSERT NOTNULL"
				assert that(actual.size() > 0, is(true))
				break
			case NULL:
				println i + "行目:" + "ASSERT NULL"
				assert that(actual.size() == 0, is(true))
				break
			case NUMBER:
				println i + "行目:" + "ASSERT NUMBER"
				assert that(actual.size(), is(cellValue.toInteger()))
				break
			default:
				throw new IllegalArgumentException("無効なキーワードです")
		}
	}
}

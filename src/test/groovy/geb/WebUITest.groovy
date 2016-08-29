package geb

import geb.util.*

import org.apache.poi.ss.usermodel.*
import org.jggug.kobo.gexcelapi.GExcel
import org.junit.*
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed.class)
public class WebUITest {
	public static class テスト{
		def file = "src/test/resources/kdt.xlsx"
		def url = "https://www.amazon.co.jp"
		Workbook book

		@Before
		void setUp() {
			book = GExcel.open(file)
		}

		@After
		void tearDown() {
			book = null
		}

		@Test
		def void "テスト"(){
			KDTcore.kdtByExcel(url, book[1])
		}
	}
}


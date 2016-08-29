import org.apache.commons.lang3.SystemUtils
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.remote.DesiredCapabilities

reportsDir = "gebreport"

private String chromeDriverPath() {
	String path
	if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
		path = "webdriver/chromedriver" // Mac環境の場合
	} else {
		path = "webdriver/chromedriver.exe" // Windows環境の場合
	}
	return path
}

driver = {
	System.setProperty('webdriver.chrome.driver', chromeDriverPath())

	def instance = new ChromeDriver(new DesiredCapabilities())
	instance.manage().window().size = new Dimension(1280, 1024)
	instance

}

environments {
	// when system property 'geb.env' is set to 'ie' use a remote IE driver
	// -Dgeb.env=ie
	'ie' {
		driver = {
			System.setProperty('webdriver.ie.driver', 'webdriver/IEDriverServer.exe')
			new InternetExplorerDriver()
		}
	}
	'firefox'{
		driver = {
			//System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe")
			new FirefoxDriver()
		}
	}
}

// 待機時間の指定（キーワード指定）
waiting {
	presets {
		slow {
			timeout = 15
			retryInterval = 1
		}
		quick {
			timeout = 1
		}
	}
}
// 待機時間の指定
waiting {
	timeout = 5
	retryInterval = 1
}
atCheckWaiting = true // at()チェックの待機設定
baseNavigatorWaiting = true // base navigatorのための待機設定
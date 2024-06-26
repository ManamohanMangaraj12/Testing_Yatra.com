package utilities;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
 
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
 
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
 
public class ExtentReportManager implements ITestListener  {
	public ExtentSparkReporter sparkReporter;  // UI of the report
	public ExtentReports extent;  //populate common info on the report
	public ExtentTest test; // creating test case entries in the report and update status of the test methods
	public void onStart(ITestContext context) {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
        String repName="Test-Report-"+timeStamp+".html";
 
		sparkReporter=new ExtentSparkReporter(System.getProperty("user.dir")+ "/reports/"+repName);//specify location of the report
		sparkReporter.config().setDocumentTitle("Yatra.com Automation Report"); // TiTle of report
		sparkReporter.config().setReportName("Yatra.com Functional Testing"); // name of the report
		sparkReporter.config().setTheme(Theme.DARK);
		extent=new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application","Yatra.com");
		extent.setSystemInfo("User Name",System.getProperty("user.name"));
		extent.setSystemInfo("Environment","QA");

		String os=context.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System ", os);
		String browser=context.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser ", browser);
		List<String> groups=context.getCurrentXmlTest().getIncludedGroups();
		if(!groups.isEmpty()) {
			extent.setSystemInfo("Groups ", groups.toString());
		}
	}
 
	public void onTestSuccess(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); // create a new entry in the report
		test.assignCategory(result.getMethod().getGroups());   //To display groups in the  reports
		test.log(Status.PASS, "Test case PASSED is: " + result.getName()); // update status p/f/s
 
//		String imgPath = Screenshots.screenShotTC(result.getName());
//		test.addScreenCaptureFromPath(imgPath);
	}
 
	public void onTestFailure(ITestResult result) {
 
		test = extent.createTest(result.getTestClass().getName()); // create a new entry in the report
		test.assignCategory(result.getMethod().getGroups());   //To display groups in the  reports
		test.log(Status.FAIL, "Test case FAILED is:" + result.getName());
		test.log(Status.INFO, "Test Case FAILED cause is: " + result.getThrowable());
 
		
		String imgPath = Screenshots.screenShotTC(result.getName());
		test.addScreenCaptureFromPath(imgPath);
	}
 
	public void onTestSkipped(ITestResult result) {
 
		test = extent.createTest(result.getTestClass().getName()); // create a new entry in the report
		test.assignCategory(result.getMethod().getGroups());   //To display groups in the  reports
		test.log(Status.SKIP, "Test case SKIPPED is:" + result.getName());
		test.log(Status.INFO, "Test Case SKIPPED cause is: " + result.getThrowable()); 
	}
 
	
	public void onFinish(ITestContext context) {
		extent.flush();
	}
}
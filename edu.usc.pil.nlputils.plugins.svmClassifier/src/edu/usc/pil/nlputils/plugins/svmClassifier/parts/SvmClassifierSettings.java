 
package edu.usc.pil.nlputils.plugins.svmClassifier.parts;

import java.io.IOException;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import edu.usc.pil.nlputils.plugins.svmClassifier.process.SvmClassifier;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;

public class SvmClassifierSettings {
	private Text txtLabel1;
	private Text txtFolderPath2;
	private Text txtFolderPath1;
	private Text txtLabel2;
	
	@Inject IEclipseContext context;
	private Text txtTestFolder1;
	private Text txtTestFolder2;
	private Text txtClassifyInput;
	private Text txtOutputFile;
	private Text txtDelimiters;
	private Text txtkVal;
	private Text txtStopWords;
	private Text txtClassifyOutput;
	private Text txtModelFilePath;
	private Text txtHashmapPath;
	public SvmClassifierSettings() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		final Shell shell = parent.getShell();
		parent.setLayout(new GridLayout(7, false));
		
		Group grpInputSettings = new Group(parent, SWT.NONE);
		grpInputSettings.setText("Training Settings");
		GridData gd_grpInputSettings = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
		gd_grpInputSettings.heightHint = 329;
		gd_grpInputSettings.widthHint = 436;
		grpInputSettings.setLayoutData(gd_grpInputSettings);
		
		Composite composite = new Composite(grpInputSettings, SWT.NONE);
		composite.setBounds(10, 20, 429, 180);
		
		Label lblLabel_1 = new Label(composite, SWT.NONE);
		lblLabel_1.setBounds(0, 35, 36, 15);
		lblLabel_1.setText("Class 2");
		
		Label lblLabel = new Label(composite, SWT.NONE);
		lblLabel.setBounds(0, 5, 36, 15);
		lblLabel.setText("Class 1");
		
		txtLabel1 = new Text(composite, SWT.BORDER);
		txtLabel1.setText("Label1");
		txtLabel1.setBounds(66, 2, 157, 21);
		
		txtLabel2 = new Text(composite, SWT.BORDER);
		txtLabel2.setText("Label2");
		txtLabel2.setBounds(66, 32, 157, 21);
		
		Label lblFolder = new Label(composite, SWT.NONE);
		lblFolder.setBounds(228, 5, 24, 15);
		lblFolder.setText("Path");
		
		Label lblFolder_1 = new Label(composite, SWT.NONE);
		lblFolder_1.setBounds(228, 35, 24, 15);
		lblFolder_1.setText("Path");
		
		txtFolderPath1 = new Text(composite, SWT.BORDER);
		txtFolderPath1.setBounds(257, 2, 143, 21);
		
		txtFolderPath2 = new Text(composite, SWT.BORDER);
		txtFolderPath2.setBounds(257, 32, 143, 21);
		
		Button button = new Button(composite, SWT.NONE);
		button.setBounds(403, 0, 21, 25);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog fd1 = new DirectoryDialog(shell);
				fd1.open();
				String fp1Directory = fd1.getFilterPath();
				txtFolderPath1.setText(fp1Directory);
				
			}
		});
		button.setText("...");
		
		Button button_1 = new Button(composite, SWT.NONE);
		button_1.setBounds(403, 30, 21, 25);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog fd2 = new DirectoryDialog(shell);
				fd2.open();
				String fp2Directory = fd2.getFilterPath();
				txtFolderPath2.setText(fp2Directory);
			
			}
		});
		button_1.setText("...");
		
		Group grpPreprocessingOptions = new Group(composite, SWT.NONE);
		grpPreprocessingOptions.setBounds(0, 56, 426, 114);
		grpPreprocessingOptions.setText("Preprocessing Options");
		
		txtStopWords = new Text(grpPreprocessingOptions, SWT.BORDER);
		txtStopWords.setBounds(178, 54, 195, 21);
		
		final Button btnLowercase = new Button(grpPreprocessingOptions, SWT.CHECK);
		btnLowercase.setBounds(25, 90, 140, 16);
		btnLowercase.setText("Convert to Lowercase");
		
		Label lblDelimiters = new Label(grpPreprocessingOptions, SWT.NONE);
		lblDelimiters.setBounds(25, 25, 55, 15);
		lblDelimiters.setText("Delimiters");
		
		txtDelimiters = new Text(grpPreprocessingOptions, SWT.BORDER);
		txtDelimiters.setText(" .,;'\\\"!-()[]{}:?");
		txtDelimiters.setBounds(178, 22, 195, 21);
		
		Button button_5 = new Button(grpPreprocessingOptions, SWT.NONE);
		button_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				txtStopWords.setText("");
				FileDialog fd = new FileDialog(shell,SWT.OPEN);
				fd.open();
				String oFile = fd.getFileName();
				String dir = fd.getFilterPath();
				txtStopWords.setText(dir+"\\"+oFile);
			}
		});
		button_5.setBounds(375, 52, 21, 25);
		button_5.setText("...");
		
		Label lblStopWordsFile = new Label(grpPreprocessingOptions, SWT.NONE);
		lblStopWordsFile.setBounds(25, 59, 98, 15);
		lblStopWordsFile.setText("Stop Words File");
		
		Button btnTermFreqencyTf = new Button(grpInputSettings, SWT.RADIO);
		btnTermFreqencyTf.setSelection(true);
		btnTermFreqencyTf.setBounds(10, 219, 199, 16);
		btnTermFreqencyTf.setText("Term Freqency TF (Word Count)");
		
		final Button btnTfidf = new Button(grpInputSettings, SWT.RADIO);
		btnTfidf.setBounds(238, 219, 90, 16);
		btnTfidf.setText("TF.IDF");
		
		final Button btnLoadModel = new Button(grpInputSettings, SWT.CHECK);
		btnLoadModel.setBounds(11, 253, 168, 16);
		btnLoadModel.setText("Load Pretrained Model");
		
		txtModelFilePath = new Text(grpInputSettings, SWT.BORDER);
		txtModelFilePath.setBounds(185, 248, 199, 21);
		
		Button button_8 = new Button(grpInputSettings, SWT.NONE);
		button_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				txtModelFilePath.setText("");
				FileDialog fd = new FileDialog(shell,SWT.OPEN);
				fd.open();
				String oFile = fd.getFileName();
				String dir = fd.getFilterPath();
				txtModelFilePath.setText(dir+"\\"+oFile);
			}
		});
		button_8.setBounds(387, 246, 20, 25);
		button_8.setText("...");
		
		txtHashmapPath = new Text(grpInputSettings, SWT.BORDER);
		txtHashmapPath.setBounds(185, 296, 199, 21);
		
		Button button_9 = new Button(grpInputSettings, SWT.NONE);
		button_9.setBounds(387, 294, 20, 25);
		button_9.setText("...");
		
		final CTabFolder tabFolder = new CTabFolder(parent, SWT.BORDER | SWT.SINGLE);
		tabFolder.setSingle(false);
		tabFolder.setSimple(false);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
		gd_tabFolder.heightHint = 214;
		gd_tabFolder.widthHint = 429;
		tabFolder.setLayoutData(gd_tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		final CTabItem tbtmTesting = new CTabItem(tabFolder, SWT.NONE);
		tbtmTesting.setText("Testing");
		
		Group grpTestMode = new Group(tabFolder, SWT.NONE);
		tbtmTesting.setControl(grpTestMode);
		
		Label lblTestFolder = new Label(grpTestMode, SWT.NONE);
		lblTestFolder.setBounds(34, 38, 95, 15);
		lblTestFolder.setText("Class 1 Test Data");
		
		txtTestFolder1 = new Text(grpTestMode, SWT.BORDER);
		txtTestFolder1.setBounds(135, 32, 220, 21);
		
		Label lblTestFolder_1 = new Label(grpTestMode, SWT.NONE);
		lblTestFolder_1.setBounds(34, 64, 95, 15);
		lblTestFolder_1.setText("Class 2 Test Data");
		
		txtTestFolder2 = new Text(grpTestMode, SWT.BORDER);
		txtTestFolder2.setBounds(135, 61, 220, 21);
		
		Button button_3 = new Button(grpTestMode, SWT.NONE);
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog fd1 = new DirectoryDialog(shell);
				fd1.open();
				String fp1Directory = fd1.getFilterPath();
				txtTestFolder1.setText(fp1Directory);
				
			}
		});
		button_3.setBounds(359, 30, 21, 25);
		button_3.setText("...");
		
		Button button_4 = new Button(grpTestMode, SWT.NONE);
		button_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog fd2 = new DirectoryDialog(shell);
				fd2.open();
				String fp2Directory = fd2.getFilterPath();
				txtTestFolder2.setText(fp2Directory);
			
			}
		});
		button_4.setBounds(359, 59, 21, 25);
		button_4.setText("...");
		
		Label lblKfoldCrossValidation = new Label(grpTestMode, SWT.NONE);
		lblKfoldCrossValidation.setBounds(34, 118, 46, 15);
		lblKfoldCrossValidation.setText("k Value");
		
		Label label = new Label(grpTestMode, SWT.NONE);
		label.setText("Output File");
		label.setBounds(10, 154, 59, 15);
		
		txtkVal = new Text(grpTestMode, SWT.BORDER);
		txtkVal.setBounds(135, 115, 220, 21);
		
		txtOutputFile = new Text(grpTestMode, SWT.BORDER);
		txtOutputFile.setBounds(135, 151, 220, 21);
		
		Button button_2 = new Button(grpTestMode, SWT.NONE);
		button_2.setBounds(359, 149, 21, 25);
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				txtOutputFile.setText("");
				FileDialog fd = new FileDialog(shell,SWT.SAVE);
				fd.open();
				String oFile = fd.getFileName();
				String dir = fd.getFilterPath();
				txtOutputFile.setText(dir+"\\"+oFile);
			}
		});
		button_2.setText("...");
		
		final Button btnCrossVal = new Button(grpTestMode, SWT.RADIO);
		btnCrossVal.setBounds(10, 94, 149, 16);
		btnCrossVal.setText("k-Fold Cross Validation");
		
		
		Button btnTrain = new Button(grpTestMode, SWT.NONE);
		btnTrain.setBounds(10, 175, 52, 25);
		btnTrain.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				long currentTime = System.currentTimeMillis();
				
				try {
				SvmClassifier svm = new SvmClassifier(btnLowercase.getSelection(), txtDelimiters.getText(), txtStopWords.getText());
				// Injecting the context into Preprocessor object so that the appendLog function can modify the Context Parameter consoleMessage
				IEclipseContext iEclipseContext = context;
				ContextInjectionFactory.inject(svm,iEclipseContext);

				int selection = tabFolder.getSelectionIndex();
				if(btnLoadModel.getSelection())
					svm.loadPretrainedModel(txtLabel1.getText(), txtLabel2.getText(), txtModelFilePath.getText(), txtHashmapPath.getText());
				else
					svm.train(txtLabel1.getText(), txtFolderPath1.getText(), txtLabel2.getText(), txtFolderPath2.getText(), btnTfidf.getSelection(), btnCrossVal.getSelection(), txtkVal.getText());
				// Cross Validation => No need to call predict and output separately
				if (!btnCrossVal.getSelection()){
				if(selection == 0){
					System.out.println("Test Mode");
					appendLog("Test Mode");
					svm.predict(txtLabel1.getText(), txtFolderPath1.getText(), txtLabel2.getText(), txtFolderPath2.getText());
					svm.output(txtLabel1.getText(), txtTestFolder1.getText(), txtLabel2.getText(), txtTestFolder2.getText(),txtOutputFile.getText());
				} else if (selection == 1){
					System.out.println("Classification Mode");
					appendLog("Classification Mode");
				}
				}
				System.out.println("Completed classification in "+((System.currentTimeMillis()-currentTime)/(double)1000)+" seconds.");
				appendLog("Completed classification in "+((System.currentTimeMillis()-currentTime)/(double)1000)+" seconds.");
				} catch (IOException | ClassNotFoundException ie) {
					ie.printStackTrace();
				}
			}
		});
		btnTrain.setText("Test");
		
		Button btnSeparateTestData = new Button(grpTestMode, SWT.RADIO);
		btnSeparateTestData.setSelection(true);
		btnSeparateTestData.setBounds(10, 10, 119, 16);
		btnSeparateTestData.setText("Separate Test Data");
		
		
		//Setting Testing as the default tab
		tabFolder.setSelection(0);
		
		CTabItem tbtmClassify = new CTabItem(tabFolder, SWT.NONE);
		tbtmClassify.setText("Classify");
		
		Group grpClassifyMode = new Group(tabFolder, SWT.NONE);
		tbtmClassify.setControl(grpClassifyMode);
		grpClassifyMode.setText("Classify");
		
		Label lblFolder_2 = new Label(grpClassifyMode, SWT.NONE);
		lblFolder_2.setBounds(10, 32, 97, 15);
		lblFolder_2.setText("Directory Path");
		
		txtClassifyInput = new Text(grpClassifyMode, SWT.BORDER);
		txtClassifyInput.setBounds(135, 26, 234, 21);
		
		Label label_1 = new Label(grpClassifyMode, SWT.NONE);
		label_1.setText("Output File");
		label_1.setBounds(10, 66, 59, 15);
		
		txtClassifyOutput = new Text(grpClassifyMode, SWT.BORDER);
		txtClassifyOutput.setBounds(135, 63, 234, 21);
		
		Button button_6 = new Button(grpClassifyMode, SWT.NONE);
		button_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				txtClassifyOutput.setText("");
				FileDialog fd = new FileDialog(shell,SWT.SAVE);
				fd.open();
				String oFile = fd.getFileName();
				String dir = fd.getFilterPath();
				txtClassifyOutput.setText(dir+"\\"+oFile);
			}
		});
		button_6.setText("...");
		button_6.setBounds(370, 61, 21, 25);
		
		Button btnClassify = new Button(grpClassifyMode, SWT.NONE);
		btnClassify.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				long currentTime = System.currentTimeMillis();
				
				try {
				SvmClassifier svm = new SvmClassifier(btnLowercase.getSelection(), txtDelimiters.getText(), txtStopWords.getText());
				// Injecting the context into Preprocessor object so that the appendLog function can modify the Context Parameter consoleMessage
				IEclipseContext iEclipseContext = context;
				ContextInjectionFactory.inject(svm,iEclipseContext);

				int selection = tabFolder.getSelectionIndex();
				if(btnLoadModel.getSelection())
					svm.loadPretrainedModel(txtLabel1.getText(), txtLabel2.getText(), txtModelFilePath.getText(), txtHashmapPath.getText());
				else
					svm.train(txtLabel1.getText(), txtFolderPath1.getText(), txtLabel2.getText(), txtFolderPath2.getText(), btnTfidf.getSelection(),false, null);
				if(selection == 0){
					System.out.println("Test Mode");
					appendLog("Test Mode");
					svm.predict(txtLabel1.getText(), txtFolderPath1.getText(), txtLabel2.getText(), txtFolderPath2.getText());
					svm.output(txtLabel1.getText(), txtTestFolder1.getText(), txtLabel2.getText(), txtTestFolder2.getText(),txtOutputFile.getText());
				} else if (selection == 1){
					System.out.println("Classification Mode");
					appendLog("Classification Mode");
					svm.classify(txtLabel1.getText(), txtLabel2.getText(), txtClassifyInput.getText());
					svm.outputPredictedOnly(txtLabel1.getText(), txtLabel2.getText(), txtClassifyInput.getText(), txtClassifyOutput.getText());
				}
				System.out.println("Completed classification in "+((System.currentTimeMillis()-currentTime)/(double)1000)+" seconds.");
				appendLog("Completed classification in "+((System.currentTimeMillis()-currentTime)/(double)1000)+" seconds.");
				} catch (IOException | ClassNotFoundException ie) {
					ie.printStackTrace();
				}

			}
		});
		btnClassify.setText("Classify");
		btnClassify.setBounds(10, 95, 52, 25);
		
		Button button_7 = new Button(grpClassifyMode, SWT.NONE);
		button_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog fd1 = new DirectoryDialog(shell);
				fd1.open();
				String fp1Directory = fd1.getFilterPath();
				txtClassifyInput.setText(fp1Directory);
				
			}
		});
		button_7.setBounds(370, 24, 21, 25);
		button_7.setText("...");
		//TODO Your code here
	}
	
	private void appendLog(String message){
		IEclipseContext parent = context.getParent();
		//System.out.println(parent.get("consoleMessage"));
		String currentMessage = (String) parent.get("consoleMessage"); 
		if (currentMessage==null)
			parent.set("consoleMessage", message);
		else {
			if (currentMessage.equals(message)) {
				// Set the param to null before writing the message if it is the same as the previous message. 
				// Else, the change handler will not be called.
				parent.set("consoleMessage", null);
				parent.set("consoleMessage", message);
			}
			else
				parent.set("consoleMessage", message);
		}
	}
}
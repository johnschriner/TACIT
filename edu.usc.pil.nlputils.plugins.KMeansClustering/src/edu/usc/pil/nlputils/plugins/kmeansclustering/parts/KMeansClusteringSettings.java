
package edu.usc.pil.nlputils.plugins.kmeansclustering.parts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.usc.pil.nlputils.plugins.preprocessorService.services.PreprocessorService;
import edu.usc.pil.nlputils.plugins.kmeansclustering.process.KMeansClustering;

public class KMeansClusteringSettings {
	private Text txtInputDir;
	private PreprocessorService ppService = new PreprocessorService();
	@Inject
	public KMeansClusteringSettings() {
		
	}
	
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		final Shell shell = parent.getShell();
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 431;
		gd_composite.heightHint = 477;
		composite.setLayoutData(gd_composite);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("Input Path");
		
		txtInputDir = new Text(composite, SWT.BORDER);
		GridData gd_txtInputDir = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtInputDir.widthHint = 244;
		txtInputDir.setLayoutData(gd_txtInputDir);
		
		button = new Button(composite, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd = new DirectoryDialog(shell);
				dd.open();
				String path = dd.getFilterPath();
				txtInputDir.setText(path);
			}
		});
		button.setText("...");
		
		lblNumberOfClusters = new Label(composite, SWT.NONE);
		lblNumberOfClusters.setText("Number of Clusters");
		
		txtNumClusters = new Text(composite, SWT.BORDER);
		new Label(composite, SWT.NONE);
		
		lblOutputPath = new Label(composite, SWT.NONE);
		lblOutputPath.setText("Output Path");
		
		txtOutputDir = new Text(composite, SWT.BORDER);
		GridData gd_txtOutputDir = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtOutputDir.widthHint = 244;
		txtOutputDir.setLayoutData(gd_txtOutputDir);
		
		button_3 = new Button(composite, SWT.NONE);
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd = new DirectoryDialog(shell);
				dd.open();
				String path = dd.getFilterPath();
				txtOutputDir.setText(path);
			}
		});
		button_3.setText("...");
		
		Button btnPreprocess = new Button(composite, SWT.NONE);
		btnPreprocess.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				showPpOptions(shell);
			}
		});
		btnPreprocess.setBounds(282, 5, 75, 25);
		btnPreprocess.setText("Preprocess...");
		
		
		Button btnCalculate = new Button(composite, SWT.NONE);
		btnCalculate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				
				ppDir = txtInputDir.getText();
				
				if(ppService.doPP) {
					
					// Injecting the context into Preprocessor object so that the appendLog function can modify the Context Parameter consoleMessage
					IEclipseContext iEclipseContext = context;
					ContextInjectionFactory.inject(ppService,iEclipseContext);

				//Preprocessing
				appendLog("Preprocessing...");
				System.out.println("Preprocessing...");
				try {
					ppDir = doPp(txtInputDir.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				}
				long startTime = System.currentTimeMillis();
				runClustering();
				appendLog("KMeans Clustering completed successfully in "+(System.currentTimeMillis()-startTime)+" milliseconds.");
			}
		});
		btnCalculate.setText("Cluster");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
	}

	private void showPpOptions(Shell shell){
		ppService.setOptions(shell);
	}
	
	private String doPp(String inputPath) throws IOException{
		return ppService.doPreprocessing(inputPath);
	}
	
	
	@Inject
	IEclipseContext context;
	private Text txtNumClusters;
	private Text txtOutputDir;
	private Label lblOutputPath;

	private Label lblNumberOfClusters;
	private Button button;

	private Button button_3;
	private String ppDir;

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
	

protected void runClustering( ){
		
		int numClusters = Integer.parseInt(txtNumClusters.getText());
	
		File dir = new File(ppDir);
		File[] listOfFiles =  dir.listFiles();
		List<File> inputFiles = new ArrayList<File>();
		for (File f : listOfFiles)
			inputFiles.add(f);
		if(inputFiles.size() == 0){
			appendLog("Please select at least one file on which to run KMeans Clustering");
			return;
		}
		
		
		System.out.println("Running KMeans Clustering...");
		appendLog("Running KMeans Clustering...");
		
		int []clusters = KMeansClustering.doClustering(inputFiles, numClusters);
		if(clusters == null)
		{
			appendLog("Sorry. Something went wrong with KMeans Clustering. Please check your input and try again.\n");
			return;
		}
		int i=0;
		appendLog("Output for KMeans Clustering");
		appendLog("Clusters formed: \n");
		
		Map<Integer, List <String> > outputClusters = new HashMap<Integer, List <String>>();
		for(i=0;i<numClusters;i++){
			outputClusters.put(i, new ArrayList<String>());
		}
		
		List<String> vec;
		i =0;
		for (int clusterNum : clusters) {
			vec = outputClusters.get(clusterNum);
			vec.add(inputFiles.get(i).getName());
			outputClusters.put(clusterNum, vec);
			i++;
		}
		
		try {
			FileWriter fw = new FileWriter(new File(txtOutputDir.getText() + "\\KMeansClusters.csv"));
			for (int c : outputClusters.keySet()) {
				System.out.printf("Cluster %d \n", c);
				appendLog("Cluster " + c + ": \n");
				vec = outputClusters.get(c);
				for(String f : vec){
					appendLog("File " + f);
					fw.write("File" + f + "\n");
				}
				appendLog("");
			}
			fw.close();
		} catch (IOException e) {
			appendLog("Error writing output to files" + e);
		}
		appendLog("\nDone KMeans Clustering...");
		
	}
}
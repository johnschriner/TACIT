package edu.usc.cssl.tacit.crawlers.frontier.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import edu.usc.cssl.tacit.common.ui.composite.from.TacitFormComposite;
import edu.usc.cssl.tacit.common.ui.corpusmanagement.services.CMDataType;
import edu.usc.cssl.tacit.common.ui.corpusmanagement.services.Corpus;
import edu.usc.cssl.tacit.common.ui.corpusmanagement.services.CorpusClass;
import edu.usc.cssl.tacit.common.ui.corpusmanagement.services.ManageCorpora;
import edu.usc.cssl.tacit.common.ui.views.ConsoleView;
import edu.usc.cssl.tacit.crawlers.frontier.services.FrontierConstants;
import edu.usc.cssl.tacit.crawlers.frontier.services.FrontierCrawl;
import edu.usc.cssl.tacit.crawlers.frontier.ui.internal.FrontierCrawlerViewImageRegistry;
import edu.usc.cssl.tacit.crawlers.frontier.ui.internal.IFrontierCrawlerUIConstants;

public class FrontierCrawlerView extends ViewPart{

		public static String ID = "edu.usc.cssl.tacit.crawlers.frontier.ui.view1";

		private ScrolledForm form;
		private FormToolkit toolkit;
		private Text queryText;
		private Text pageText;
		private Text corpusNameTxt;
		private static Button btnAnswer;
		Combo domainList;
		Button checkPages;
		private boolean[] jsonFilter = new boolean[6];
		private Table senatorTable;
		private Button addSenatorBtn;
		private ElementListSelectionDialog listDialog;
		private List<String> selectedRepresentatives;
		private static Button btnQuestion;
		private Button removeSenatorButton;
		private static Button btnComment;
		private Text answerCount;
		private Text commentCount;
		String subredditText;
		int redditCount = 1;
		String oldSubredditText;
		ArrayList<String> content;
		private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		private DateTime fromDate, toDate;
		private Date maxDate;
		private Date minDate;
		private Button dateRange;
		private Button Creation, Activity, Votes;  
		final String[] domains = new String[]{"Science","Health","Engineering","Humanities and Social Sciences"};

		@Override
		public void createPartControl(Composite parent) {
			toolkit = new FormToolkit(parent.getDisplay());
			form = toolkit.createScrolledForm(parent);
			toolkit.decorateFormHeading(form.getForm());
			form.setImage(FrontierCrawlerViewImageRegistry.getImageIconFactory().getImage(IFrontierCrawlerUIConstants.IMAGE_STACK_OBJ));
			form.setText("Frontier Journal Crawler");

			GridLayoutFactory.fillDefaults().applyTo(form.getBody());

			Section section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED);

			GridDataFactory.fillDefaults().grab(true, false).span(3, 1).applyTo(section);
			section.setExpanded(true);

			ScrolledComposite sc = new ScrolledComposite(section, SWT.H_SCROLL | SWT.V_SCROLL);
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);

			GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(sc);
			TacitFormComposite.addErrorPopup(form.getForm(), toolkit);

			Section inputSection = toolkit.createSection(form.getBody(),
					Section.TITLE_BAR | Section.EXPANDED | Section.DESCRIPTION);

			GridDataFactory.fillDefaults().span(3, 1).applyTo(inputSection);
			inputSection.setExpanded(true);
			inputSection.setText("Input Details"); //$NON-NLS-1$

			ScrolledComposite inputsc = new ScrolledComposite(inputSection, SWT.H_SCROLL | SWT.V_SCROLL);
			inputsc.setExpandHorizontal(true);
			inputsc.setExpandVertical(true);

			GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(inputsc);

			Composite InputSectionClient = toolkit.createComposite(inputSection);
			inputsc.setContent(InputSectionClient);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(inputsc);
			GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(InputSectionClient);
			inputSection.setClient(InputSectionClient);

			Label domain = new Label(InputSectionClient, SWT.NONE);
			domain.setText("Select Field:");
			GridDataFactory.fillDefaults().grab(false, false).span(1, 0).applyTo(domain);
			
			domainList = new Combo(InputSectionClient, SWT.FLAT | SWT.READ_ONLY);
			GridDataFactory.fillDefaults().grab(true, false).span(2, 0).applyTo(domainList);
			toolkit.adapt(domainList);
			domainList.setItems(domains);
			domainList.select(0);
			
			Label sortType = new Label(InputSectionClient, SWT.NONE);
			sortType.setText("Select sub-domains*:");
			senatorTable = new Table(InputSectionClient, SWT.BORDER | SWT.MULTI);
			GridDataFactory.fillDefaults().grab(true, true).span(1, 3).hint(90, 50).applyTo(senatorTable);

			
			Composite buttonComp = new Composite(InputSectionClient, SWT.NONE);
			GridLayout btnLayout = new GridLayout();
			btnLayout.marginWidth = btnLayout.marginHeight = 0;
			btnLayout.makeColumnsEqualWidth = false;
			buttonComp.setLayout(btnLayout);
			buttonComp.setLayoutData(new GridData(GridData.FILL_VERTICAL));

			addSenatorBtn = new Button(buttonComp, SWT.PUSH); // $NON-NLS-1$
			addSenatorBtn.setText("Add...");
			GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(addSenatorBtn);

			addSenatorBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
//					final String mainArray[] = StackConstants.sortTypes;
					ILabelProvider lp = new ArrayLabelProvider();
					listDialog = new ElementListSelectionDialog(addSenatorBtn.getShell(), lp);
					listDialog.setTitle("Select domain");
					listDialog.setMessage("Type the name of the domain");
					listDialog.setMultipleSelection(true);
					listDialog.setElements(FrontierConstants.sites.get(domainList.getSelectionIndex()));
					if (listDialog.open() == Window.OK) {
						updateTable(listDialog.getResult());
					}
				}

			});
			addSenatorBtn.setEnabled(true);

			removeSenatorButton = new Button(buttonComp, SWT.PUSH);
			removeSenatorButton.setText("Remove...");
			GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(removeSenatorButton);
			removeSenatorButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					for (TableItem item : senatorTable.getSelection()) {
						selectedRepresentatives.remove(item.getText());
						item.dispose();
					}
					if (selectedRepresentatives.size() == 0) {
						removeSenatorButton.setEnabled(false);
					}
				}
			});
			removeSenatorButton.setEnabled(false);

			Composite inputSec = new Composite(InputSectionClient, SWT.None);
			GridDataFactory.fillDefaults().grab(true, false).span(3, 0).indent(0, 0).applyTo(inputSec);
			GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(inputSec);

			TacitFormComposite.createEmptyRow(toolkit, InputSectionClient);

//			Composite inputSec1 = new Composite(InputSectionClient, SWT.None);
//			GridDataFactory.fillDefaults().grab(true, false).span(3, 0).indent(0, 0).applyTo(inputSec1);
//			GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(inputSec1);
//
//			createStoredAttributesSection(toolkit, inputSec1, form.getMessageManager());

			Label dummy1 = toolkit.createLabel(InputSectionClient, "", SWT.NONE);
			GridDataFactory.fillDefaults().grab(false, false).span(3, 0).applyTo(dummy1);

			Group dateGroup = new Group(InputSectionClient, SWT.SHADOW_IN);
			GridDataFactory.fillDefaults().grab(true, false).span(4, 0).applyTo(dateGroup);
			dateGroup.setText("Filter Results");
			GridLayoutFactory.fillDefaults().numColumns(3).applyTo(dateGroup);

			final Composite limitClient = new Composite(dateGroup, SWT.None);
			GridDataFactory.fillDefaults().grab(true, false).span(1, 1).indent(10, 10).applyTo(limitClient);
			GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(limitClient);

			checkPages = new Button(limitClient, SWT.CHECK);
			GridDataFactory.fillDefaults().grab(true, false).span(2, 0).applyTo(checkPages);
			checkPages.setText("Limit Pages");
			
			Label limitPages = new Label(limitClient, SWT.NONE);
			limitPages.setText("Limit records per topic:");
			GridDataFactory.fillDefaults().grab(false, false).span(1, 0).applyTo(limitPages);
			pageText = new Text(limitClient, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).span(1, 0).applyTo(pageText);
			pageText.setEnabled(false);	
			
			checkPages.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(checkPages.getSelection())
						pageText.setEnabled(true);
					else
						pageText.setEnabled(false);
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});

//			dateRange = new Button(dateGroup, SWT.CHECK);
//			GridDataFactory.fillDefaults().grab(true, false).span(4, 0).indent(10, 10).applyTo(dateRange);
//			dateRange.setText("Specify Date Range");
//			final Composite dateRangeClient = new Composite(dateGroup, SWT.None);
//			GridDataFactory.fillDefaults().grab(true, false).span(1, 1).indent(10, 10).applyTo(dateRangeClient);
//			GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).applyTo(dateRangeClient);
//			dateRangeClient.setEnabled(false);
//			dateRangeClient.pack();
//
//			final Label fromLabel = new Label(dateRangeClient, SWT.NONE);
//			fromLabel.setText("From:");
//			GridDataFactory.fillDefaults().grab(false, false).span(1, 0).applyTo(fromLabel);
//			fromDate = new DateTime(dateRangeClient, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
//			GridDataFactory.fillDefaults().grab(false, false).span(1, 0).applyTo(fromDate);
//			fromLabel.setEnabled(false);
//			fromDate.setEnabled(false);
//
//			fromDate.addListener(SWT.Selection, new Listener() {
//				@Override
//				public void handleEvent(Event event) {
//					int day = fromDate.getDay();
//					int month = fromDate.getMonth() + 1;
//					int year = fromDate.getYear();
//					Date newDate = null;
//					try {
//						newDate = format.parse(day + "/" + month + "/" + year);
//					} catch (java.text.ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					if (newDate.before(minDate) || newDate.after(maxDate)) {
//						Calendar cal = Calendar.getInstance();
//						cal.setTime(minDate);
//						fromDate.setMonth(cal.get(Calendar.MONTH));
//						fromDate.setDay(cal.get(Calendar.DAY_OF_MONTH));
//						fromDate.setYear(cal.get(Calendar.YEAR));
//					}
//				}
//			});
//
//			final Label toLabel = new Label(dateRangeClient, SWT.NONE);
//			toLabel.setText("To:");
//			GridDataFactory.fillDefaults().grab(false, false).span(1, 0).applyTo(toLabel);
//			toDate = new DateTime(dateRangeClient, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
//			GridDataFactory.fillDefaults().grab(false, false).span(1, 0).applyTo(toDate);
//			toLabel.setEnabled(false);
//			toDate.setEnabled(false);
//
//			toDate.addListener(SWT.Selection, new Listener() {
//				@Override
//				public void handleEvent(Event event) {
//					int day = toDate.getDay();
//					int month = toDate.getMonth() + 1;
//					int year = toDate.getYear();
//					Date newDate = null;
//					try {
//						newDate = format.parse(day + "/" + month + "/" + year);
//					} catch (java.text.ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					if (newDate.after(maxDate) || newDate.before(minDate)) {
//						Calendar cal = Calendar.getInstance();
//						cal.setTime(maxDate);
//						toDate.setMonth(cal.get(Calendar.MONTH));
//						toDate.setDay(cal.get(Calendar.DAY_OF_MONTH));
//						toDate.setYear(cal.get(Calendar.YEAR));
//					}
//				}
//			});
//
//			dateRange.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					if (dateRange.getSelection()) {
//						dateRangeClient.setEnabled(true);
//						fromLabel.setEnabled(true);
//						fromDate.setEnabled(true);
//						toLabel.setEnabled(true);
//						toDate.setEnabled(true);
//					} else {
//						dateRangeClient.setEnabled(false);
//						fromLabel.setEnabled(false);
//						fromDate.setEnabled(false);
//						toLabel.setEnabled(false);
//						toDate.setEnabled(false);
//					}
//				}
//			});

			TacitFormComposite.createEmptyRow(toolkit, dateGroup);

			Composite client = toolkit.createComposite(form.getBody());
			GridLayoutFactory.fillDefaults().equalWidth(true).numColumns(1).applyTo(client); // Align
																								// the
																								// composite
																								// section
																								// to
																								// one
																								// column
			GridDataFactory.fillDefaults().grab(true, false).span(1, 1).applyTo(client);

			TacitFormComposite.createEmptyRow(toolkit, client);
			corpusNameTxt = TacitFormComposite.createCorpusSection(toolkit, client, form.getMessageManager());

			addButtonsToToolBar();
		}

		@Override
		public void setFocus() {
			form.setFocus();
		}

		public void updateTable(Object[] result) {
			if (selectedRepresentatives == null) {
				selectedRepresentatives = new ArrayList<String>();
			}

			for (Object object : result) {
				if (!selectedRepresentatives.contains((String) object))
					selectedRepresentatives.add((String) object);
			}
			// Collections.sort(selectedSenators);
			senatorTable.removeAll();
			for (String itemName : selectedRepresentatives) {
				TableItem item = new TableItem(senatorTable, 0);
				item.setText(itemName);
				if (!removeSenatorButton.isEnabled()) {
					removeSenatorButton.setEnabled(true);
				}
			}

		}

		static class ArrayLabelProvider extends LabelProvider {
			@Override
			public String getText(Object element) {
				return (String) element;
			}
		}

		private boolean canItProceed() {
			form.getMessageManager().removeAllMessages();

			try{
			if(selectedRepresentatives.isEmpty()){
				form.getMessageManager().addMessage("DomainError", "Enter atleast one domain name", null,
						IMessageProvider.ERROR);
				return false;
			}else{
				form.getMessageManager().removeMessage("DomainError");
			}
			}catch(Exception e){
				form.getMessageManager().addMessage("DomainError", "Enter atleast one domain name", null,
						IMessageProvider.ERROR);
				return false;
			}
			try {
				int pages = Integer.parseInt(pageText.getText());
				if (pages < 1) {
					form.getMessageManager().addMessage("pageLimit", "Enter the number of pages to be crawled", null,
							IMessageProvider.ERROR);
					return false;
				} else
					form.getMessageManager().removeMessage("pageLimit");
			} catch (Exception e) {
				form.getMessageManager().addMessage("pageLimit", "Enter the number of pages to be crawled", null,
						IMessageProvider.ERROR);
				return false;
			}

			/*
			 * String message =
			 * OutputPathValidation.getInstance().validateOutputDirectory(
			 * outputLayout.getOutputLabel().getText(), "Output"); if (message !=
			 * null) { message = outputLayout.getOutputLabel().getText() + " " +
			 * message; form.getMessageManager().addMessage("output", message,
			 * null,IMessageProvider.ERROR); return false; } else {
			 * form.getMessageManager().removeMessage("output"); }
			 */

			// Validate corpus name
			String corpusName = corpusNameTxt.getText();
			if (null == corpusName || corpusName.isEmpty()) {
				form.getMessageManager().addMessage("corpusName", "Provide corpus name", null, IMessageProvider.ERROR);
				return false;
			} else {
				String outputDir = IFrontierCrawlerUIConstants.DEFAULT_CORPUS_LOCATION + File.separator + corpusName;
				if (new File(outputDir).exists()) {
					form.getMessageManager().addMessage("corpusName", "Corpus already exists", null,
							IMessageProvider.ERROR);
					return false;
				} else {
					form.getMessageManager().removeMessage("corpusName");
					return true;
				}
			}
		}

		private void addButtonsToToolBar() {
			IToolBarManager mgr = form.getToolBarManager();
			mgr.add(new Action() {
				@Override
				public ImageDescriptor getImageDescriptor() {
					return (FrontierCrawlerViewImageRegistry.getImageIconFactory().getImageDescriptor(IFrontierCrawlerUIConstants.IMAGE_LRUN_OBJ));
					
				}

				@Override
				public String getToolTipText() {
					return "Crawl";
				}

				String outputDir;
				String corpusName;
				Corpus corpus;
				int pages;
				String tags;
				boolean canProceed;
				boolean isDate;
				int ansLimit, comLimit;
				Long from, to;
				String crawlOrder;
				
				@Override
				public void run() {
					final Job job = new Job("Frontier Journal Crawler") {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							TacitFormComposite.setConsoleViewInFocus();
							TacitFormComposite.updateStatusMessage(getViewSite(), null, null, form);
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									if(checkPages.getSelection())
										pages = Integer.parseInt(pageText.getText());
									else
										pages =-1;
									corpusName = corpusNameTxt.getText();
									isDate = dateRange.getSelection();
									jsonFilter[0] = questionUserBtn.getSelection();
									jsonFilter[1] = ansUserBtn.getSelection();
									jsonFilter[2] = commentUserBtn.getSelection();
									jsonFilter[3] = isAnsweredBtn.getSelection();
									jsonFilter[4] = answerBodyBtn.getSelection();
									jsonFilter[5] = commentBodyBtn.getSelection();
									ansLimit = Integer.parseInt(answerCount.getText().trim());
									comLimit = Integer.parseInt(commentCount.getText().trim());
//									if (isDate) {
//										System.out.print("_____________________________");
//										Calendar cal = Calendar.getInstance();
//										cal.set(fromDate.getYear(), fromDate.getMonth(), fromDate.getDay());
//										from = cal.getTimeInMillis() / 1000;
//										cal.set(toDate.getYear(), toDate.getMonth(), toDate.getDay());
//										to = cal.getTimeInMillis() / 1000;
//										System.out.println(from + "   " + to);
//									}
									outputDir = IFrontierCrawlerUIConstants.DEFAULT_CORPUS_LOCATION + File.separator+ corpusName.trim();
									if (!new File(outputDir).exists()) {
										new File(outputDir).mkdirs();
									}
								}
							});

							int progressSize =selectedRepresentatives.size()*pages + 30;
							monitor.beginTask("Running Frontier Journal Crawler...", progressSize);
							TacitFormComposite.writeConsoleHeaderBegining("Frontier Journal Crawler started");
							FrontierCrawl crawler = new FrontierCrawl();
							monitor.subTask("Initializing...");
							monitor.worked(10);
							if (monitor.isCanceled())
								handledCancelRequest("Crawling is Stopped");
							corpus = new Corpus(corpusName, CMDataType.FRONTIER_JSON);
							for (final String domain : selectedRepresentatives) {
								outputDir = IFrontierCrawlerUIConstants.DEFAULT_CORPUS_LOCATION + File.separator + corpusName;
								outputDir += File.separator + domain;
								if (!new File(outputDir).exists()) {
									new File(outputDir).mkdirs();
								}

								try {
									monitor.subTask("Crawling...");
									if (monitor.isCanceled())
										return handledCancelRequest("Crawling is Stopped");
									crawler.crawl(outputDir, domain, pages);
									if (monitor.isCanceled())
										return handledCancelRequest("Crawling is Stopped");
								} catch (Exception e) {
									return handleException(monitor, e, "Crawling failed. Provide valid data");
								}
								try {
									Display.getDefault().syncExec(new Runnable() {

										@Override
										public void run() {

											CorpusClass cc = new CorpusClass(domain, outputDir);
											cc.setParent(corpus);
											corpus.addClass(cc);

										}
									});
								} catch (Exception e) {
									e.printStackTrace();
									return Status.CANCEL_STATUS;
								}
							}
							ManageCorpora.saveCorpus(corpus);
							if (monitor.isCanceled())
								return handledCancelRequest("Crawling is Stopped");

							monitor.worked(100);
							monitor.done();
							return Status.OK_STATUS;
						}
					};
					job.setUser(true);
					canProceed = canItProceed();
					if (canProceed) {
						job.schedule(); // schedule the job
						job.addJobChangeListener(new JobChangeAdapter() {

							public void done(IJobChangeEvent event) {
								if (!event.getResult().isOK()) {
									TacitFormComposite
											.writeConsoleHeaderBegining("Error: <Terminated> Frontier Journal Crawler  ");
									TacitFormComposite.updateStatusMessage(getViewSite(), "Crawling is stopped",
											IStatus.INFO, form);

								} else {
									TacitFormComposite
											.writeConsoleHeaderBegining("Success: <Completed> Frontier Journal Crawler  ");
									TacitFormComposite.updateStatusMessage(getViewSite(), "Crawling is completed",
											IStatus.INFO, form);
									ConsoleView.printlInConsoleln("Done");
									ConsoleView.printlInConsoleln("Frontier Journal Crawler completed successfully.");

								}
							}
						});
					}
				}
			});

			Action helpAction = new Action() {
				@Override
				public ImageDescriptor getImageDescriptor() {
					return (FrontierCrawlerViewImageRegistry.getImageIconFactory().getImageDescriptor(IFrontierCrawlerUIConstants.IMAGE_HELP_CO));
				}

				@Override
				public String getToolTipText() {
					return "Help";
				}

				@Override
				public void run() {
					PlatformUI.getWorkbench().getHelpSystem().displayHelp("edu.usc.cssl.tacit.crawlers.stackexchange.ui.stackexchange");
				};
			};

			mgr.add(helpAction);
			PlatformUI.getWorkbench().getHelpSystem().setHelp(helpAction, "edu.usc.cssl.tacit.crawlers.stackexchange.ui.stackexchange");
			PlatformUI.getWorkbench().getHelpSystem().setHelp(form, "edu.usc.cssl.tacit.crawlers.stackexchange.ui.stackexchange");
			form.getToolBarManager().update(true);
		}

		private IStatus handledCancelRequest(String message) {
			TacitFormComposite.updateStatusMessage(getViewSite(), message, IStatus.INFO, form);
			ConsoleView.printlInConsoleln("Frontier Journal Crawler cancelled.");
			return Status.CANCEL_STATUS;
		}

		private IStatus handleException(IProgressMonitor monitor, Exception e, String message) {
			monitor.done();
			System.out.println(message);
			e.printStackTrace();
			TacitFormComposite.updateStatusMessage(getViewSite(), message, IStatus.ERROR, form);
			return Status.CANCEL_STATUS;
		}

		static Button ansUserBtn, answerBodyBtn, questionTitleBtn, questionBodyBtn, questionUserBtn, commentBodyBtn,
				isAnsweredBtn, commentUserBtn;

		public static void createStoredAttributesSection(FormToolkit toolkit, Composite parent,
				final IMessageManager mmng) {
			Section section = toolkit.createSection(parent,
					Section.TITLE_BAR | Section.EXPANDED | Section.DESCRIPTION | Section.TWISTIE);

			GridDataFactory.fillDefaults().grab(true, false).span(1, 1).applyTo(section);
			GridLayoutFactory.fillDefaults().numColumns(4).applyTo(section);
			section.setText("Stored Attributes "); //$NON-NLS-1$
			section.setDescription("Choose values for Filter");

			ScrolledComposite sc = new ScrolledComposite(section, SWT.H_SCROLL | SWT.V_SCROLL);
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);

			GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(sc);

			Composite sectionClient = toolkit.createComposite(section);
			sc.setContent(sectionClient);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(sc);
			GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(sectionClient);
			section.setClient(sectionClient);

			Label dummy = toolkit.createLabel(sectionClient, "", SWT.NONE);
			GridDataFactory.fillDefaults().grab(false, false).span(3, 0).applyTo(dummy);

			questionUserBtn = new Button(sectionClient, SWT.CHECK);
			questionUserBtn.setText("User Details for Questions");
			GridDataFactory.fillDefaults().grab(true, false).span(1, 0).applyTo(questionUserBtn);
			questionUserBtn.setEnabled(true);

			ansUserBtn = new Button(sectionClient, SWT.CHECK);
			ansUserBtn.setText("User Details for Answers");
			GridDataFactory.fillDefaults().grab(true, false).span(1, 0).applyTo(ansUserBtn);
			ansUserBtn.setEnabled(true);

			commentUserBtn = new Button(sectionClient, SWT.CHECK);
			commentUserBtn.setText("User Details for Comments");
			GridDataFactory.fillDefaults().grab(true, false).span(1, 0).applyTo(commentUserBtn);
			commentUserBtn.setEnabled(true);

			isAnsweredBtn = new Button(sectionClient, SWT.CHECK);
			isAnsweredBtn.setText("Is Question Answered");
			GridDataFactory.fillDefaults().grab(true, false).span(1, 0).applyTo(isAnsweredBtn);
			isAnsweredBtn.setEnabled(true);

			answerBodyBtn = new Button(sectionClient, SWT.CHECK);
			answerBodyBtn.setText("Answer Text");
			GridDataFactory.fillDefaults().grab(true, false).span(1, 0).applyTo(answerBodyBtn);
			answerBodyBtn.setEnabled(true);

			commentBodyBtn = new Button(sectionClient, SWT.CHECK);
			commentBodyBtn.setText("Comment Text");
			GridDataFactory.fillDefaults().grab(true, false).span(1, 0).applyTo(commentBodyBtn);
			commentBodyBtn.setEnabled(true);

		}


	}

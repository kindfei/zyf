package zyf.sm.gui;


import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.layout.GridData;

import zyf.sm.bean.ConnectionInfo;
import zyf.sm.util.Executor;
import zyf.sm.util.Helper;

public class MainShell {
	private static final Log log = Helper.getLog(MainShell.class);

	private Shell shell = null;
	private Button bPuTTY = null;
	private Button bPuTTYjp = null;
	private Button bWinSCP = null;
	private Button bSecureCRT = null;
	private Button bTeraTerm = null;
	private Tree tree = null;
	private Label lMessage = null;

	private TreeItem rootItem;
	private TreeItem currentItem;
	private TreeItem dragSourceItem;
	private TreeItem copyItem;

	private Display display;
	
	private boolean readDoc = true;
	
	private Image folderIcon;
	private Image folderOpenIcon;
	private Image fileIcon;
	
	private Image newFileIcon;
	private Image newFolderIcon;
	private Image copyIcon;
	private Image cutIcon;
	private Image pasteIcon;
	private Image refreshIcon;
	private Image editIcon;
	private Image deleteIcon;
	
	public void display() {
        display = new Display();
        
    	folderIcon = new Image(display, Helper.getResource("icon/folder.gif"));
    	folderOpenIcon = new Image(display, Helper.getResource("icon/folder_open.gif"));
    	fileIcon = new Image(display, Helper.getResource("icon/file.gif"));

    	newFileIcon = new Image(display, Helper.getResource("icon/new_file.gif"));
    	newFolderIcon = new Image(display, Helper.getResource("icon/new_folder.gif"));
    	copyIcon = new Image(display, Helper.getResource("icon/copy.gif"));
    	cutIcon = new Image(display, Helper.getResource("icon/cut.gif"));
    	pasteIcon = new Image(display, Helper.getResource("icon/paste.gif"));
    	refreshIcon = new Image(display, Helper.getResource("icon/refresh.gif"));
    	editIcon = new Image(display, Helper.getResource("icon/edit.gif"));
    	deleteIcon = new Image(display, Helper.getResource("icon/delete.gif"));
    	
        try {
			createShell(display);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			readDoc = false;
			createShell(display);
			lMessage.setText("Read connection.xml error, wrong format in it.");
		}
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
               display.sleep();
            }
        }
        display.dispose();
	}

	private void createShell(Display display) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		shell = new Shell(display);
		shell.setImage(new Image(display, Helper.getResource("icon/icon.ico")));
		shell.setText("Session Manager");
		shell.setLayout(gridLayout);
		shell.setSize(new Point(500, 350));
		
		GridData gdbPuTTY = new GridData();
		gdbPuTTY.heightHint = 20;
		gdbPuTTY.widthHint = 65;
		
		GridData gdbPuTTYjp = new GridData();
		gdbPuTTYjp.heightHint = 20;
		gdbPuTTYjp.widthHint = 65;
		
		GridData gdbWinSCP = new GridData();
		gdbWinSCP.heightHint = 20;
		gdbWinSCP.widthHint = 65;
		
		GridData gdbSecureCRT = new GridData();
		gdbSecureCRT.heightHint = 20;
		gdbSecureCRT.widthHint = 65;
		
		GridData gdbTeraTerm = new GridData();
		gdbTeraTerm.heightHint = 20;
		gdbTeraTerm.widthHint = 65;
		
		GridData gdTree = new GridData(GridData.FILL_BOTH);
		gdTree.horizontalSpan = 8;
		
		GridData gdlMessage = new GridData(GridData.FILL_HORIZONTAL);
		gdlMessage.horizontalSpan = 5;
		gdlMessage.heightHint = 15;
		
		bPuTTY = new Button(shell, SWT.NONE);
		bPuTTY.setText("PuTTY");
		bPuTTY.setLayoutData(gdbPuTTY);
		bPuTTY.addMouseListener(
				new MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				Object obj = currentItem.getData();
				if (obj instanceof String) return;
				lMessage.setText(Executor.runPuTTYen((ConnectionInfo)obj));
			}
		});
		
		bWinSCP = new Button(shell, SWT.NONE);
		bWinSCP.setText("WinSCP");
		bWinSCP.setLayoutData(gdbWinSCP);
		bWinSCP.addMouseListener(new MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				Object obj = currentItem.getData();
				if (obj instanceof String) return;
				lMessage.setText(Executor.runWinSCP((ConnectionInfo)obj));
			}
		});
		
		bPuTTYjp = new Button(shell, SWT.NONE);
		bPuTTYjp.setText("PuTTYjp");
		bPuTTYjp.setLayoutData(gdbPuTTYjp);
		bPuTTYjp.addMouseListener(
				new MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				Object obj = currentItem.getData();
				if (obj instanceof String) return;
				lMessage.setText(Executor.runPuTTYjp((ConnectionInfo)obj));
			}
		});
		
		bTeraTerm = new Button(shell, SWT.NONE);
		bTeraTerm.setText("TeraTerm");
		bTeraTerm.setLayoutData(gdbSecureCRT);
		if (Executor.isTeraTermInstalled()) {
			bTeraTerm.addMouseListener(new MouseAdapter() {
				public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
					Object obj = currentItem.getData();
					if (obj instanceof String) return;
					lMessage.setText(Executor.runTeraTerm((ConnectionInfo)obj));
				}
			});
		} else {
			bTeraTerm.setEnabled(false);
		}
		
		bSecureCRT = new Button(shell, SWT.NONE);
		bSecureCRT.setText("SecureCRT");
		bSecureCRT.setLayoutData(gdbSecureCRT);
		if (Executor.isSecureCRTInstalled()) {
			bSecureCRT.addMouseListener(new MouseAdapter() {
				public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
					Object obj = currentItem.getData();
					if (obj instanceof String) return;
					lMessage.setText(Executor.runSecureCRT((ConnectionInfo)obj));
				}
			});
		} else {
			bSecureCRT.setEnabled(false);
		}
		
		tree = new Tree(shell, SWT.BORDER);
		tree.setLayoutData(gdTree);
		tree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				lMessage.setText("");
				currentItem = (TreeItem) e.item;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		tree.addTreeListener(new org.eclipse.swt.events.TreeListener() {
			public void treeCollapsed(org.eclipse.swt.events.TreeEvent e) {
				TreeItem item = (TreeItem)e.item;
				if (item.getData().equals("folder"))
					item.setImage(folderIcon);
			}
			public void treeExpanded(org.eclipse.swt.events.TreeEvent e) {
				TreeItem item = (TreeItem)e.item;
				if (item.getData().equals("folder"))
					item.setImage(folderOpenIcon);
			}
		
		});
		tree.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				Object obj = currentItem.getData();
				if (obj instanceof String) return;
				lMessage.setText(Executor.runPuTTYen((ConnectionInfo)obj));
			}
		});
		tree.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.DEL) {
					delete();
				} else if (e.keyCode == SWT.F5) {
					refresh();
				} else if (e.keyCode == 'c' && (e.stateMask & SWT.CTRL) != 0) {
					copy();
				} else if (e.keyCode == 'x' && (e.stateMask & SWT.CTRL) != 0) {
					cut();
				} else if (e.keyCode == 'v' && (e.stateMask & SWT.CTRL) != 0) {
					paste();
				} else if (e.keyCode == 'e' && (e.stateMask & SWT.CTRL) != 0) {
					if (currentItem.getData() instanceof String) {
						editFolder();
					} else {
						edit();
					}
				}
			}
			public void keyReleased(KeyEvent e) {
			}
		});
		addDragSupport();
		addDropSupport();
		setMenu();
		setItem();
		
		lMessage = new Label(shell, SWT.NONE);
		lMessage.setText("");
		lMessage.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		lMessage.setLayoutData(gdlMessage);
	}
	
	private void addDragSupport() {
		final DragSource dragSource = new DragSource(tree, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
		dragSource.setTransfer(new Transfer[]{ConnectionInfoTransfer.getInstance()});
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragStart(DragSourceEvent event) {
				TreeItem[] selection = tree.getSelection();
				if (selection.length > 0 && selection[0].getData() instanceof ConnectionInfo) {
					event.doit = true;
					dragSourceItem = selection[0];
				} else {
					event.doit = false;
				}
			};

			public void dragSetData(DragSourceEvent event) {
				if (ConnectionInfoTransfer.getInstance().isSupportedType(event.dataType))
					event.data = dragSourceItem.getData();
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE) dragSourceItem.dispose();
				dragSourceItem = null;
				saveDocument();
			}
		});
	}
	
	private void addDropSupport() {
		final DropTarget dropTarget = new DropTarget(tree, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
		dropTarget.setTransfer(new Transfer[]{ConnectionInfoTransfer.getInstance()});
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
			}

			public void dropAccept(DropTargetEvent event) {
				if (event.item == null || !(((TreeItem)event.item).getData() instanceof String))
					event.detail = DND.DROP_NONE;
			}

			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				
				ConnectionInfo info = (ConnectionInfo)event.data;
				
				TreeItem item = new TreeItem((TreeItem)event.item, SWT.NULL);
				item.setText(info.getName() + " (" + info.getUser() + "@" + info.getHost() + ")");
				item.setData(info);
				item.setImage(fileIcon);
			}
		});
	}
	
	private void setMenu() {
		Menu menu = new Menu(tree);
		
	    MenuItem newSession = new MenuItem(menu, SWT.CASCADE);
	    newSession.setText("New Session");
	    newSession.setImage(newFileIcon);
	    newSession.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				add();
			}
		});
	    
	    MenuItem newFolder = new MenuItem(menu, SWT.CASCADE);
	    newFolder.setText("New Folder");
	    newFolder.setImage(newFolderIcon);
	    newFolder.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				addFolder();
			}
		});
	    
		new MenuItem(menu, SWT.SEPARATOR);
		
	    MenuItem copy = new MenuItem(menu, SWT.CASCADE);
	    copy.setText("&Copy\tCtrl+C");
	    copy.setImage(copyIcon);
	    copy.setAccelerator (SWT.CTRL + 'C');
	    copy.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				copy();
			}
		});
	    
	    MenuItem cut = new MenuItem(menu, SWT.CASCADE);
	    cut.setText("Cu&t\tCtrl+X");
	    cut.setImage(cutIcon);
	    cut.setAccelerator (SWT.CTRL + 'X');
	    cut.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				cut();
			}
		});
	    
	    MenuItem paste = new MenuItem(menu, SWT.CASCADE);
	    paste.setText("&Paste\tCtrl+V");
	    paste.setImage(pasteIcon);
	    paste.setAccelerator (SWT.CTRL + 'V');
	    paste.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				paste();
			}
		});
	    
	    new MenuItem(menu, SWT.SEPARATOR);
	    
	    MenuItem refresh = new MenuItem(menu, SWT.CASCADE);
	    refresh.setText("&Refresh\tF5");
	    refresh.setImage(refreshIcon);
	    refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				refresh();
			}
		});
	    
	    new MenuItem(menu, SWT.SEPARATOR);
	    
	    MenuItem edit = new MenuItem(menu, SWT.CASCADE);
	    edit.setText("&Edit\tCtrl+E");
	    edit.setImage(editIcon);
	    paste.setAccelerator (SWT.CTRL + 'E');
	    edit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (currentItem.getData() instanceof String) {
					editFolder();
				} else {
					edit();
				}
			}
		});
	    
	    MenuItem delete = new MenuItem(menu, SWT.CASCADE);
	    delete.setText("&Delete\tDelete");
	    delete.setImage(deleteIcon);
	    delete.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				delete();
			}
		});
	    
	    tree.setMenu(menu);
	}
	
	private void setItem() {
		rootItem = new TreeItem(tree, SWT.NULL);
		rootItem.setText("Sessions");
		rootItem.setData("folder");
		rootItem.setImage(folderOpenIcon);
		currentItem = rootItem;
		
		Document doc = Helper.readDocument();
		if (doc == null || !readDoc) return;
		documentWalk(doc.getRootElement(), rootItem);
		rootItem.setExpanded(true);
	}
	
	private void documentWalk(Element root, TreeItem parent) {
		TreeItem item = null;
        for (int i = 0, size = root.nodeCount(); i < size; i++) {
            Node node = root.node(i);
            if (node instanceof Element) {
                Element element = (Element)node;
            	String name = element.getName();
            	if (name.equals("folder")) {
            		item = new TreeItem((TreeItem)parent, SWT.NULL);
            		item.setText(element.attribute("name").getText());
            		item.setData("folder");
            		item.setImage(folderOpenIcon);
                    documentWalk(element, item);
                    item.setExpanded(true);
            	} else {
            		ConnectionInfo info = new ConnectionInfo();
            		info.setName(element.attribute("name").getText());
            		info.setType(element.attribute("type").getText());
            		info.setPort(element.attribute("port").getText());
            		info.setHost(element.attribute("host").getText());
            		info.setUser(element.attribute("user").getText());
            		info.setPassword(element.attribute("password").getText());

            		item = new TreeItem((TreeItem)parent, SWT.NULL);
            		item.setText(info.getName() + " (" + info.getUser() + "@" + info.getHost() + ")");
            		item.setData(info);
            		item.setImage(fileIcon);
            	}
            }
        }
	}
	
	private void saveDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("connections");
		treeWalk(tree.getItem(0), root);
		Helper.writeDocument(document);
	}
	
	private void treeWalk(TreeItem root, Element parent) {
		TreeItem[] items = root.getItems();
		for (int i=0; i < items.length; i++) {
			TreeItem item = items[i];
			Object obj = item.getData();
			if (obj instanceof String) {
				Element element = parent.addElement("folder");
				element.addAttribute("name", item.getText());
				treeWalk(item, element);
			} else {
				Element element = parent.addElement("session");
				ConnectionInfo info = (ConnectionInfo)obj;
				element.addAttribute("name", info.getName());
				element.addAttribute("type", info.getType());
				element.addAttribute("port", info.getPort());
				element.addAttribute("host", info.getHost());
				element.addAttribute("user", info.getUser());
				element.addAttribute("password", info.getPassword());
			}
		}
	}
	
	private boolean copy() {
		if (currentItem == rootItem) {
			lMessage.setText("Cann't copy root item");
			return false;
		}
		copyItem = currentItem;
		isCut = false;
		return true;
	}
	
	boolean isCut;
	private void cut() {
		if (copy()) isCut = true;
	}
	
	private void paste() {
		if (copyItem == null || copyItem == currentItem) {
			lMessage.setText("Nothing to paste");
			return;
		}
		
		if (currentItem.getData() instanceof ConnectionInfo) {
			lMessage.setText("Cann't paste to session item");
			return;
		}
		
		TreeItem[] items = copyItem.getItems();
		for (int i=0; i < items.length; i++) {
			if (items[i] == currentItem) {
				lMessage.setText("Cann't copy parent to son");
				return;
			}
		}
		
		TreeItem root = new TreeItem(currentItem, SWT.NULL);
		root.setText(copyItem.getText());
		root.setData(Helper.copy(copyItem.getData()));
		root.setImage(copyItem.getImage());
		
		treeWalkForPaste(copyItem, root);
		
		if (isCut) {
			copyItem.dispose();
			isCut = false;
		}
		copyItem = null;
		
		saveDocument();
	}
	
	private void treeWalkForPaste(TreeItem root, TreeItem parent) {
		TreeItem[] items = root.getItems();
		for (int i=0; i < items.length; i++) {
			TreeItem item = items[i];
			
			TreeItem son = new TreeItem((TreeItem)parent, SWT.NULL);
			son.setText(item.getText());
			Object data = Helper.copy(item.getData());
			son.setData(data);
			son.setImage(item.getImage());
			
			if (data instanceof String) {
				treeWalkForPaste(item, son);
			}
		}
	}
	
	private void refresh() {
		rootItem.dispose();
		setItem();
	}
	
	private void add() {
		if (!(currentItem.getData() instanceof String)) {
			currentItem = currentItem.getParentItem();
		}
		currentItem.setExpanded(true);
		SessionDialog diaog = new SessionDialog(shell);
		ConnectionInfo info = diaog.open(currentItem, SessionDialog.add);
		if (info == null) return;
		
		TreeItem item = new TreeItem(currentItem, SWT.NULL);
		item.setText(info.getName() + " (" + info.getUser() + "@" + info.getHost() + ")");
		item.setData(info);
		item.setImage(fileIcon);
		saveDocument();
	}
	
	private void addFolder() {
		if (!(currentItem.getData() instanceof String)) {
			currentItem = currentItem.getParentItem();
		}
		currentItem.setExpanded(true);
		TreeItem item = new TreeItem(currentItem, SWT.NULL);
		item.setText("New Folder");
		item.setData("folder");
		item.setImage(folderIcon);
		currentItem = item;
		editFolder();
	}
	
	private void edit() {
		SessionDialog diaog = new SessionDialog(shell);
		ConnectionInfo info = diaog.open(currentItem, SessionDialog.edit);
		if (info == null) return;
		
		currentItem.setText(info.getName() + " (" + info.getUser() + "@" + info.getHost() + ")");
		currentItem.setData(info);
		currentItem.setImage(fileIcon);
		saveDocument();
	}
	
	private void editFolder() {
		if (currentItem == null || currentItem == rootItem) {
			lMessage.setText("Cann't edit root item");
			return;
		}
		
		final TreeItem item = currentItem;
		
		final Text text = new Text(tree, SWT.BORDER);
		text.setText(item.getText());
		
		final TreeEditor editor = new TreeEditor(tree);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		editor.setEditor(text, item);
		
		text.selectAll();
		
		Listener textListener = new Listener() {
			public void handleEvent(final Event e) {
				switch (e.type) {
					case SWT.FocusOut:
						item.setText(text.getText());
						text.dispose();
						saveDocument();
						break;
	
					case SWT.Traverse:
						switch (e.detail) {
							case SWT.TRAVERSE_RETURN:
								item.setText(text.getText());
								text.dispose();
								saveDocument();
							case SWT.TRAVERSE_ESCAPE:
								text.dispose();
								e.doit = false;
								saveDocument();
						}
						break;
				}
			}
		};

		text.addListener(SWT.FocusOut, textListener);
		text.addListener(SWT.Traverse, textListener);

		text.setFocus();
	}
	
	private void delete() {
		if (currentItem == rootItem) {
			lMessage.setText("Cann't delete root item");
			return;
		}
		MessageBox box = new MessageBox(shell, SWT.YES | SWT.NO);
		box.setMessage("Are you sure to delete : " + currentItem.getText());
		if (box.open() == SWT.YES) {
			currentItem.dispose();
			saveDocument();
		}
	}
}

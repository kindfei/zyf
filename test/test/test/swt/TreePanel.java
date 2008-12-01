package test.swt;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.layout.GridData;

public class TreePanel extends Composite {

	private Tree tree = null;

	public TreePanel(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.heightHint = 230;
		gridData.widthHint = 122;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		tree = new Tree(this, SWT.NONE);
//		tree.addSelectionListener(new SelectionListener() {
//	        public void widgetSelected(SelectionEvent e) {
//	            TreeItem item = (TreeItem) e.item;
//	            String file = (String) item.getData();
//	            System.out.println(file);
//	        }
//	        public void widgetDefaultSelected(SelectionEvent e) {
//	            TreeItem item = (TreeItem) e.item;
//	            String file = (String) item.getData();
//	            System.out.println(file);
//	        }
//	    });
	    tree.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				// Locates the File position in the Tree.
				Point point = new Point(event.x, event.y);
				final TreeItem item = tree.getItem(point);
				if (item == null)
					return;

				final Text text = new Text(tree, SWT.BORDER);
				text.setText(item.getText());
//				text.setBackground(new Color(null, null));

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
							break;

						case SWT.Traverse:
							switch (e.detail) {
							case SWT.TRAVERSE_RETURN:
								item.setText(text.getText());
							case SWT.TRAVERSE_ESCAPE:
								text.dispose();
								e.doit = false;
							}
							break;
						}
					}
				};

				text.addListener(SWT.FocusOut, textListener);
				text.addListener(SWT.Traverse, textListener);

				text.setFocus();
			}
		});


		tree.setLayoutData(gridData);
		tree.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				System.out.println("mouseDoubleClick()"); // TODO Auto-generated Event stub mouseDoubleClick()
			}
		});
		this.setLayout(gridLayout);
		this.setSize(new Point(153, 257));
		setItem();
	}
	
	private void setItem() {
		TreeItem item = new TreeItem(tree, SWT.NULL);
		item.setText("Session");
		item.setData("folder");
		for (int i=0; i<5; i++) {
			item = new TreeItem(item, SWT.NULL);
			item.setText("Session");
			item.setData(i + "");
		}
	}

}

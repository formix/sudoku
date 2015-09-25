package com.girfid.sudoku.gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.formix.sudoku.Cell;
import org.formix.sudoku.GridUpdaterCallback;
import org.formix.sudoku.Solver;
import org.formix.sudoku.Sudoku;
import org.formix.sudoku.SudokuDownloader;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.girfid.sudoku.logging.JListLogger;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import net.miginfocom.swing.MigLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JList;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import javax.swing.JScrollPane;

public class SudokuSolver {

	private static final int SOLVER_REFRESH_RATE = 397;

	private JFileChooser fileChooser;
	private JFrame frame;
	private JPanel buttonsPanel;
	private JButton btnLoad;
	private JButton btnSave;
	private JButton btnGenerate;
	private JButton btnSolve;
	private Solver solver;
	private GridUpdaterCallback callBack;
	private JComboBox<SudokuLevels> levelComboBox;
	private JButton btnGetFromWeb;
	private JButton btnCancel;
	private JPanel generatePanel;
	private JSplitPane splitPane;
	private SudokuGrid sudokuGrid;
	private JListLogger logger;
	private JScrollPane scrollPane;
	private JList<String> list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SudokuSolver window = new SudokuSolver();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SudokuSolver() {
		initialize();
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.logger = new JListLogger(this.list);
		this.solver = new Solver(this.logger);
		this.callBack = new GridUpdaterCallback(SOLVER_REFRESH_RATE) {
			@Override
			public void updateGrid(final Sudoku sudoku) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						sudokuGrid.setSudokuGrid(sudoku);
					}
				});
			}
		};
		this.frame.pack();
		this.centerInScreen();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					@Override
					public boolean dispatchKeyEvent(KeyEvent e) {
						if ((e.getKeyCode() & KeyEvent.VK_CONTROL) == KeyEvent.VK_CONTROL) {
							if (e.getID() == KeyEvent.KEY_PRESSED) {
								sudokuGrid.setCompositionMode(true);
							} else if (e.getID() == KeyEvent.KEY_RELEASED) {
								sudokuGrid.setCompositionMode(false);
							}
						}
						return false;
					}
				});

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				sudokuGrid.setCompositionMode(true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				sudokuGrid.setCompositionMode(false);
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				saveCurrentSudoku();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				loadLastSudoku();
			}
		});
		frame.setTitle(Messages.getString("SudokuSolver.0")); //$NON-NLS-1$
		frame.setBounds(100, 100, 551, 469);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		buttonsPanel = new JPanel();
		frame.getContentPane().add(buttonsPanel, BorderLayout.WEST);

		btnLoad = new JButton(Messages.getString("SudokuSolver.1")); //$NON-NLS-1$
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGrid();
			}
		});
		buttonsPanel.setLayout(new MigLayout("", "[69.00px]", //$NON-NLS-1$ //$NON-NLS-2$
				"[25px][25px][25px][25px][]")); //$NON-NLS-1$
		btnLoad.setMnemonic('l');
		buttonsPanel.add(btnLoad, "cell 0 0,growx,aligny center"); //$NON-NLS-1$

		btnSave = new JButton(Messages.getString("SudokuSolver.6")); //$NON-NLS-1$
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveGrid();
			}
		});
		btnSave.setMnemonic('s');
		buttonsPanel.add(btnSave, "cell 0 1,growx,aligny center"); //$NON-NLS-1$

		btnSolve = new JButton(Messages.getString("SudokuSolver.8")); //$NON-NLS-1$
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solveGrid();
			}
		});
		btnSolve.setMnemonic('o');
		buttonsPanel.add(btnSolve, "cell 0 2,growx,aligny center"); //$NON-NLS-1$

		JButton btnClear = new JButton(Messages.getString("SudokuSolver.10")); //$NON-NLS-1$
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearGrid();
			}
		});
		btnClear.setMnemonic('c');
		buttonsPanel.add(btnClear, "cell 0 3,growx,aligny center"); //$NON-NLS-1$

		JButton btnEmpty = new JButton(Messages.getString("SudokuSolver.12")); //$NON-NLS-1$
		btnEmpty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createEmptyGrid();
			}
		});
		btnEmpty.setMnemonic('e');
		buttonsPanel.add(btnEmpty, "cell 0 4,growx,aligny center");

		generatePanel = new JPanel();
		frame.getContentPane().add(generatePanel, BorderLayout.NORTH);

		JLabel lblLevel = new JLabel(Messages.getString("SudokuSolver.14")); //$NON-NLS-1$
		generatePanel.add(lblLevel);

		levelComboBox = new JComboBox<SudokuLevels>();
		levelComboBox.setModel(new DefaultComboBoxModel<SudokuLevels>(
				SudokuLevels.values()));
		levelComboBox.setSelectedIndex(0);
		generatePanel.add(levelComboBox);

		btnGetFromWeb = new JButton(Messages.getString("SudokuSolver.2")); //$NON-NLS-1$
		btnGetFromWeb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getFromWeb();
			}
		});
		btnGetFromWeb.setMnemonic('l');
		generatePanel.add(btnGetFromWeb);

		btnGenerate = new JButton(Messages.getString("SudokuSolver.16")); //$NON-NLS-1$
		generatePanel.add(btnGenerate);
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateGrid();
			}
		});
		btnGenerate.setMnemonic('g');

		btnCancel = new JButton(Messages.getString("SudokuSolver.17")); //$NON-NLS-1$
		generatePanel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopSolver();
			}
		});
		btnCancel.setEnabled(false);
		btnCancel.setMnemonic('a');

		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		sudokuGrid = new SudokuGrid();
		splitPane.setLeftComponent(sudokuGrid);

		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(600, 124));
		splitPane.setRightComponent(scrollPane);

		list = new JList<String>();
		scrollPane.setViewportView(list);
	}

	protected void loadLastSudoku() {
		File folder = new File(System.getProperty("user.home") //$NON-NLS-1$
				+ "/.sudokusolver"); //$NON-NLS-1$
		if (folder.isDirectory()) {
			folder.mkdir();
		}
		File sudokuFile = new File(folder, "current.sdks"); //$NON-NLS-1$
		if (sudokuFile.exists()) {
			try {
				this.sudokuGrid.load(sudokuFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void saveCurrentSudoku() {
		File folder = new File(System.getProperty("user.home") //$NON-NLS-1$
				+ "/.sudokusolver"); //$NON-NLS-1$
		if (!folder.isDirectory()) {
			folder.mkdir();
		}
		File sudokuFile = new File(folder, "current.sdks"); //$NON-NLS-1$
		try {
			this.sudokuGrid.getSudokuGrid().save(sudokuFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void createEmptyGrid() {
		this.sudokuGrid.setSudokuGrid(new Sudoku());
		this.logger.clear();
	}

	protected void stopSolver() {
		this.solver.stop();
	}

	protected void generateGrid() {
		toggleButtonsEnabled(buttonsPanel);
		toggleButtonsEnabled(generatePanel);
		this.sudokuGrid.clear();
		this.sudokuGrid.setEnabled(false);

		Thread t = new Thread(new Runnable() {
			public void run() {
				final Sudoku grid = solver.solve(new Sudoku());
				List<Cell> cells = grid.getWritableCells();
				Collections.shuffle(cells);
				int i = 0;
				for (Cell cell : cells) {
					if (i < 55) {
						cell.clear();
					} else {
						cell.setEditable(false);
					}
					i++;
				}

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						sudokuGrid.setSudokuGrid(grid);
						toggleButtonsEnabled(buttonsPanel);
						toggleButtonsEnabled(generatePanel);
						sudokuGrid.setEnabled(true);
					}
				});
			}
		});
		t.start();
	}

	protected void getFromWeb() {
		this.sudokuGrid.clear();
		toggleButtonsEnabled(buttonsPanel);
		toggleButtonsEnabled(generatePanel);

		Thread t = new Thread(new Runnable() {
			public void run() {
				SudokuLevels level = (SudokuLevels) levelComboBox
						.getSelectedItem();
				final Sudoku grid = SudokuDownloader.createFromWeb(level
						.getLevel());

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						sudokuGrid.setSudokuGrid(grid);
						toggleButtonsEnabled(buttonsPanel);
						toggleButtonsEnabled(generatePanel);
						sudokuGrid.setEnabled(true);
						btnGetFromWeb.setEnabled(true);
						levelComboBox.setEnabled(true);
						btnGenerate.setEnabled(true);
					}
				});
			}
		});
		t.start();
	}

	private void toggleButtonsEnabled(JPanel panel) {
		final int COMP_COUNT = panel.getComponentCount();
		for (int i = 0; i < COMP_COUNT; i++) {
			Component comp = panel.getComponent(i);
			comp.setEnabled(!comp.isEnabled());
		}

	}

	protected void clearGrid() {
		this.sudokuGrid.clear();
		this.logger.clear();
	}

	protected void solveGrid() {
		this.sudokuGrid.setEnabled(false);
		toggleButtonsEnabled(buttonsPanel);
		toggleButtonsEnabled(generatePanel);

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Sudoku result = solver.solve(sudokuGrid.getSudokuGrid(),
							callBack);
					sudokuGrid.setSudokuGrid(result);
				} catch (Exception e) {
					logger.info(e.toString());
					e.printStackTrace();
				} finally {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							toggleButtonsEnabled(buttonsPanel);
							toggleButtonsEnabled(generatePanel);
							sudokuGrid.setEnabled(true);
						}
					});
				}
			}
		});
		t.start();
	}

	protected void saveGrid() {
		fileChooser.setDialogTitle(Messages.getString("SudokuSolver.24")); //$NON-NLS-1$
		int ret = fileChooser.showSaveDialog(frame);
		if (ret == JFileChooser.APPROVE_OPTION) {
			if (fileChooser.getSelectedFile() != null) {
				try {
					this.sudokuGrid.getSudokuGrid().save(
							fileChooser.getSelectedFile().getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void loadGrid() {
		fileChooser.setDialogTitle(Messages.getString("SudokuSolver.25")); //$NON-NLS-1$
		int ret = fileChooser.showOpenDialog(frame);
		if (ret == JFileChooser.APPROVE_OPTION) {
			if (fileChooser.getSelectedFile() != null) {
				try {
					this.sudokuGrid.load(fileChooser.getSelectedFile()
							.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void centerInScreen() {
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int w = frame.getSize().width;
		int h = frame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Move the window
		frame.setLocation(x, y);
	}
}

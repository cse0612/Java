package �������ý��۱���;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


/* 				�ܺ�Ŭ����  (JFrame)				*/
public class CustomerSystem extends JFrame {	
	
	public static final String string = null;

	//���� Ŭ���� ��ü ���� => ��ü���� �������� ����
	Menumain menumain = new Menumain();
	West west = new West();
	Showtable showtable = new Showtable();
	Buttons buttons = new Buttons();
	
	int updateRow;		// '����' '����', '����' Button Mouse Event���� ���
	int juminDoubleCheck; //�ֹε�Ϲ�ȣ �˻� ���� ����
	//�ܺ�Ŭ���� ������ 
	public CustomerSystem() {
		
		OUTTER: while(true) {
			//�̹��� jpg���� ��Ʈ�� ȭ������ ����
			ImageIcon icon = new ImageIcon("images/intro.jpg");
			
			JOptionPane.showMessageDialog(null, null, "�����������ý���", JOptionPane.NO_OPTION, icon);
			
			//�н����� ����â ����
			String password = JOptionPane.showInputDialog("�������ý���" + "\n" + "�н������Է�");
			//String password = "1111";
			String passwd = "1111";
			
			if(password.equals(passwd)) {
				setTitle("�������ý���");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//�����ư�� JFrame ������ �����Ŵ (������ �����ư������ ������ ������������)
				
				//Build GUI >>>   JFrame 'BorderLayout'�� Panel Component �����ϱ� 
				add(menumain.mb, BorderLayout.NORTH);
				add(west, BorderLayout.WEST);
				add(buttons, BorderLayout.SOUTH);
				add(showtable.scroll, BorderLayout.CENTER);
				
				setSize(1000, 800);
				setLocation(0, 0);
				setVisible(true);
				
				break OUTTER;
			} else {
				JOptionPane.showMessageDialog(null, "�н����尡 ���� �ʽ��ϴ� \n 'Ȯ��' ��ư�� ��������", "�н����� ��������", JOptionPane.ERROR_MESSAGE);
				continue OUTTER;
			}
		}
	}
	
	/* 				 ����Ŭ���� (�޴�)				*/
	class Menumain extends JPanel implements ItemListener {
		JMenuBar 			mb;
		JMenu 				file, sort, help;
		JMenuItem			fopen, fsave, exit, proinfo;
		JCheckBoxMenuItem	snum, sname, slocation, sjob;
		
		FileDialog			readOpen, saveOpen;
		String 				readFileName, saveFileName;
		
		
		public Menumain() {
			//MenuBar (Menu�� �ֻ��� Component)
			mb = new JMenuBar();			
			//Menu title
			file = new JMenu("����");		
			sort = new JMenu("����");
			help = new JMenu("����");
			//Menu item
			fopen = new JMenuItem("����");
			fsave = new JMenuItem("����");
			exit = new JMenuItem("�ݱ�");
			//���� CheckBox menu item
			snum = new JCheckBoxMenuItem("��ȣ");
			sname = new JCheckBoxMenuItem("�̸�");
			slocation = new JCheckBoxMenuItem("�������");
			sjob = new JCheckBoxMenuItem("����");
			//���� menu item
			proinfo = new JMenuItem("���α׷�����");
			//�޴��� ���� menu item �߰�
			file.add(fopen); file.add(fsave);  file.addSeparator(); file.add(exit);
			sort.add(snum);	sort.add(sname); sort.add(slocation); sort.add(sjob); 
			help.add(proinfo);
			//MenuBar�� Menu title �߰�
			mb.add(file); mb.add(sort); mb.add(help);
		
			//////////////   ���� ���� �̺�Ʈ   ////////////////////////
			fopen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//StringTokenizer Class ��� 
					StringTokenizer st;
					Vector<String> vec;
					
					readOpen = new FileDialog(CustomerSystem.this, "���Ͽ���", FileDialog.LOAD);  //CustomerSystem.this -> Parent JFrame
					readOpen.setVisible(true);
					
					readFileName =  readOpen.getDirectory() + "\\" + readOpen.getFile();	//�������ϰ��
					
					//IOException(FileNotFoundException ����)
					try {
						//BufferedReader ó��(���ڿ� line���� ó�� �� ���۸� ���� �ӵ� ����)
						BufferedReader br = new BufferedReader(new FileReader(readFileName));
						showtable.data.removeAllElements(); //data ���� �ʱ�ȭ
						String line = "";
						while((line = br.readLine()) != null) {
							//StringTokenizer, Vector�� ����Ҷ����� ��ü ����
							st = new StringTokenizer(line, ", ");
							//Vector �ʱ�ȭ
							vec = new Vector<String>();
							while(st.hasMoreTokens()) {		// Token�� ������ ���� 
								vec.add(st.nextToken()); 	//���Ϳ� �߰�
							}
							
							//showtable Ŭ���� 2����Vector���� data�� vector�� �߰�
							showtable.data.addElement(vec);
						}
						//��������� �ش� Listener �� ������� ����
						showtable.datamodel.fireTableDataChanged();
						br.close();
					} catch(IOException ex) {
						System.out.println(ex);
					}
				}
			});
			//////////////   ���� ���� �̺�Ʈ   ////////////////////////
			fsave.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// Frame parent -> JFrame ��ü�� ����� �θ�Ŭ���� ����
					saveOpen = new FileDialog(CustomerSystem.this, "���Ͽ���", FileDialog.SAVE);
					saveOpen.setVisible(true);
					
					saveFileName = saveOpen.getDirectory() + "//" + saveOpen.getFile();
					//System.out.println(saveFileName);
					
					String str = "";  // ��ü�� ����
					String temp = ""; // ���� 
					
					//IOException, FileNotFoundException �� ���� ����ó�� �ʼ�
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(saveFileName));
						
						for(int i = 0; i < showtable.table.getRowCount(); i++) {	//table�� ��(row)�� ������ŭ �ݺ�ó��
							//2���� �迭�� ���ͺ��� data���� ����Ÿ�� �������� �ż��� => elementAt
							temp = showtable.data.elementAt(i).toString();
							//[name, hp, juminnumber] -> �յ� [] ������ ���� ó��    &  ����ó��
							str += temp.substring(1, temp.length()-1) + "\n";	
						}
						bw.write(str);
						bw.close();
					} catch(Exception ex) {
						System.out.println(ex);
					}
				}
			});
			//////////////   �޴� ���� �̺�Ʈ   ////////////////////////
			exit.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			
			// ���� ��ư �̺�Ʈ ����
			snum.addItemListener(this);
			sname.addItemListener(this);
			slocation.addItemListener(this);
			sjob.addItemListener(this);
			
			//���� 
			proinfo.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new newWindow();	// Ŭ���� newWindow 
				}
			});
		}
		//////////////   ���� �̺�Ʈ   ////////////////////////
		@Override
		public void itemStateChanged(ItemEvent e) {
			
			int index = 0;
			if(e.getSource().equals(snum)) 		index = 0;   
			if(e.getSource().equals(sname)) 	index = 1;	
			if(e.getSource().equals(slocation)) index = 7;	
			if(e.getSource().equals(sjob)) 		index = 9;	
			
			int sortIndex = index;
			
			int row = showtable.table.getRowCount();	// ���� ����
			int col = showtable.table.getColumnCount(); // ���� ����
			
			String[][] arr = new String[row][col];		// JTable row,col������ �迭�ʱ�ȭ   
			
			//JTable�� ������ ��ü��  2���� �迭�� ���
			for(int i = 0; i < row; i++) {
				for(int j = 0; j < col; j++) {
					arr[i][j] = (String) showtable.table.getValueAt(i, j);
				}
			}
			
			//���Ĺ�� - Arrays sort()�ż��� lambda ���ٽĿ����� final ����������(������ ����� ������ �Ұ���)
			if (sortIndex == 0)   Arrays.sort(arr, (a, b) -> Integer.compare(Integer.parseInt(a[sortIndex]), Integer.parseInt(b[sortIndex])));
			else Arrays.sort(arr, (a, b) -> a[sortIndex].compareTo(b[sortIndex])); 
			
			//Jtable�� ���ĵ� ����Ÿ ���
			for(int i = 0; i < row; i++) {
				for(int j = 0; j < col; j++) {
					showtable.table.setValueAt(arr[i][j], i, j);
				}
			}
			//üũ�ڽ� ��������
			snum.setSelected(false);
			sname.setSelected(false);
			slocation.setSelected(false);
			sjob.setSelected(false);
		}
	}

	/*			West �гι�ġ	 (����Ŭ����) 			*/ 
	class West extends JPanel {

		Input input = new Input();		//�Է�Ŭ����
		Output output = new Output();	//���Ŭ����
		
		public West() {
			setLayout(new BorderLayout());		//���̾ƿ�����
			add(input, BorderLayout.CENTER);	//�Է��г���ġ�߰�
			add(output, BorderLayout.SOUTH);	//����г���ġ
		}	
	}
	/*			�Է� �гι�ġ	 (����Ŭ����) 			*/
	class Input extends JPanel {
		JLabel					la, la2;
		JTextField				tf[];	// JTextField ������Ʈ ��ü���� �ּҰ��� ������ �迭
		JComboBox<String>		cb;
		JButton					juminBtn;
		
		public Input() {
			//���� ���� �����
			LineBorder line = new LineBorder(Color.ORANGE, 2);
			setBorder(new TitledBorder(line, "�Է�"));
			
			String[] text = {"��ȣ", "�̸�", "�ڵ�����ȣ", "�̸���", "�ֹε�Ϲ�ȣ"};
			tf = new JTextField[5];
			juminBtn = new JButton("�ߺ��˻�");
			juminBtn.setPreferredSize(new Dimension(10,10));
			//GridLayout ��ġ  
			setLayout(new GridLayout(6, 3, 10, 30)); //  GridLayout(int rows, int cols, int hgap, int vgap)
			for(int i = 0; i < text.length; i++) {
				la = new JLabel(text[i]);
				tf[i] = new JTextField();		
				la.setHorizontalAlignment(JLabel.CENTER);
				add(la);  add(tf[i]);   
				if(i == text.length-1)	add(juminBtn); 
				else 					add(new JLabel(""));	
			}
			
			la2 = new JLabel("����");
			la2.setHorizontalAlignment(JLabel.CENTER);
			String[] cbText = {"����", "ȸ���", "�л�"};
			cb = new JComboBox<String>(cbText);
			add(la2); add(cb); add(new JLabel());
			
			//�ֻ��� �����̳��� JFrame ���� �κ����� ����� �����Ҷ��� Dimension ���
			setPreferredSize(new Dimension(360, 200));
			setBackground(Color.WHITE);
			
			///  �ߺ��˻� ��ư �̺�Ʈ  /////
			juminBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int juminCheck = 1;	//��üũ��
					
//					String[] chkArr = {"��ȣ", "�̸�", "�ڵ�����ȣ", "�̸���", "�ֹε�Ϲ�ȣ"};
					
//					for(int i = 0; i < showtable.data.size(); i++) {
//						for (int j = 0; j < chkArr.length; j++) {
//							if(showtable.data.elementAt(i).get(j).contains(tf[j].getText().trim())) {
//								JOptionPane.showMessageDialog(null, "��ϵ� " + chkArr[j] + "(��)�� �̹� �����մϴ�", "�ߺ� ���� Message", JOptionPane.ERROR_MESSAGE);
//								juminCheck = 0;
//								west.input.tf[j].setText(null);
//								west.input.tf[j].requestFocus();
//								return;
//							}
//						}
//					}
					
					
					for(int i = 0; i < showtable.data.size(); i++) {
						if(showtable.data.elementAt(i).get(4).contains(tf[4].getText().trim())) {	
							JOptionPane.showMessageDialog(null, "��ϵ� �ֹε�Ϲ�ȣ�� �̹� �����մϴ�", "�ֹι�ȣ �ߺ�����", JOptionPane.ERROR_MESSAGE);
							juminCheck = 0;
							west.input.tf[4].setText(null);
							west.input.tf[4].requestFocus();
							return;
						}
					}
					if(juminCheck == 1) {
						JOptionPane.showMessageDialog(null, "�ߺ��� ���� �����ϴ�!", "�ߺ� üũ �޽���", JOptionPane.INFORMATION_MESSAGE);
						west.input.tf[4].setEnabled(false);
						west.input.juminBtn.setEnabled(false);
						juminDoubleCheck = 1;	//�������� �ʱ�ȭ (�߰���ư �̺�Ʈ�� �ߺ��˻����� ���� üũ��)
					}
				}
			});
			
		}
	}
	/*			��� �г� �� �����˻� CardLayout ��ġ	 (����Ŭ����) 			*/
	class Output extends JPanel {
		JLabel lbLeft;	//�Ż����� ���� label
		JLabel lbRight[];	//�Ż����� ���� label
		
		JPanel info = new JPanel();	  // '�Ż�����'ī�带 ����� ���� �г�
		JPanel search = new JPanel(); // '�����˻�'ī�带 ����� ���� �г�	
		CardLayout card;
		JRadioButton searchRadio[] = new JRadioButton[4];
		JTextField findText = new JTextField(10);
		JButton searchBtn, exitBtn;
		
		
		public Output() {
			// ī�巹�̾ƿ� ����
			card = new CardLayout();
			setLayout(card);
			
			////// �Ż����� ī�� ���̾ƿ�
			info.setBackground(Color.WHITE);
			info.setBorder(new TitledBorder(new LineBorder(Color.ORANGE, 2), "�Ż�����"));
			//GridLayout ��ġ  
			info.setLayout(new GridLayout(4, 2, 10, 5));
			
			String[] text = {"��   ��", "��    ��", "�������", "��    ��"};
			lbRight = new JLabel[4];
			for(int i = 0; i < text.length; i++) {
				lbLeft = new JLabel(text[i]);
				lbRight[i] = new JLabel();
				info.add(lbLeft);
				info.add(lbRight[i]);
			}
		
			info.setPreferredSize(new Dimension(360, 300));
			add(info,"ù��°ī��");
			
			///// �����˻� ī�� ���̾ƿ�
			search.setBackground(Color.ORANGE);
			search.setBorder(new TitledBorder(new LineBorder(Color.WHITE, 2), "�����˻�"));
			
			ButtonGroup group = new ButtonGroup();
			String search_name[] = {"�̸�", "����", "�������", "�������"};
			
			searchBtn = new JButton("ã   ��");
			searchBtn.setBackground(Color.GREEN);
			exitBtn = new JButton("������");
			exitBtn.setBackground(Color.GRAY);
			
			//������ġ
			search.setLayout(null);
			int x = -70;
			search.setPreferredSize(new Dimension(360,200));
			
			for(int i = 0; i < searchRadio.length; i++) {
				searchRadio[i] = new JRadioButton(search_name[i]);
				searchRadio[i].setBounds(x += 80, 30, 80, 30);  // (int x, int y, int width, int height)
				searchRadio[i].setBackground(Color.ORANGE);

				group.add(searchRadio[i]);
				search.add(searchRadio[i]);
			}
			
			findText.setBounds(25, 80, 200, 30);
			searchBtn.setBounds(25, 130, 70, 30);
			exitBtn.setBounds(115, 130, 110, 30);
			search.add(findText);
			search.add(searchBtn);
			search.add(exitBtn);

			add(search, "�ι�°ī��");
			
			
			/////  ������ �̺�Ʈ ����   ////////////////////////
			exitBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//JTable�� �������� ��� 
					showtable.datamodel.setDataVector(showtable.data, showtable.columnNames); 
					//�˻���ưȰ��ȭ 
					buttons.searchBtn.setEnabled(true); 
					//ī�巹�̾ƿ����� 
					west.output.card.next(west.output); 
					findText.setText(null);
					showtable.setTable();
					if(west.input.tf[0].getText().isEmpty()) {
						west.output.lbRight[0].setText(null);
						west.output.lbRight[1].setText(null);
						west.output.lbRight[2].setText(null);
						west.output.lbRight[3].setText(null);
					}
				}
			});
			
			//ã�� �̺�Ʈ ����
			searchBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int flag = 1;
					// ����üũ�ڽ� ����üũ
					for(int i=0; i < searchRadio.length; i++) {
						if(searchRadio[i].isSelected()) {
							flag = 0;
							break;
						}
					}
					
					if(flag > 0) {
						JOptionPane.showMessageDialog(null, "�˻� ������ �����ϼ���", "���޽���", JOptionPane.ERROR_MESSAGE);
					} else if(findText.getText().length() == 0) {
						JOptionPane.showMessageDialog(null, "�˻����� �Է����ּ���", "���޽���", JOptionPane.ERROR_MESSAGE);
					} else {
						//JTable���� �˻��� 2���迭�� �����Ͽ� �ѷ��ֱ� ���� �ӽú��� ���
						Vector<Vector<String>> findData = new Vector<Vector<String>>();
						
						// ������ �����ڽ��� JTable�� �˻��� ���� �ε����� �ʱ�ȭ
						int num = 0;
						if(searchRadio[0].isSelected()) num = 1;	//�̸�
						if(searchRadio[1].isSelected()) num = 9;	//����
						if(searchRadio[2].isSelected()) num = 7;	//�������
						if(searchRadio[3].isSelected()) num = 8;	//����
						
						for(int i = 0; i < showtable.data.size(); i++) {
							//2���� Vector�� data���� ���� �ش� ���� �Է°��� ���ԵǾ� �ִ��� üũ�Ѵ� 
							if(showtable.data.elementAt(i).get(num).contains(findText.getText().trim())) {
								//�ӽ� 2���� ���Ϳ� �߰�
								findData.addElement(showtable.data.elementAt(i));
							}
						}
						//���Ϳ� �ƹ��͵� ���ٸ� (�˻��� ���� �������)
						if(findData.isEmpty()) {
							JOptionPane.showMessageDialog(null, "�˻��Ͻ� ������ �����ϴ�", "���޽���", JOptionPane.ERROR_MESSAGE);
							//showtable.datamodel.setDataVector(showtable.data, showtable.columnNames);
							findText.setText(null);
						} else {
						// JTable datamodel�� 2���� ���ͳֱ�
						// �÷� ���Ϳ� ������ ���� ��ġ��
							showtable.datamodel.setDataVector(findData, showtable.columnNames);
							showtable.datamodel.fireTableDataChanged();
							showtable.setTable();
						}
						
					}
				}
			});
			
			
		}
	}

	/*			JTable ���	 (����Ŭ����) 			 
	����Ŭ���� () Mouse�������̽��� Listner�� ���� ��� �޼ҵ带 �� �����ؾ� ������ 
	�ϳ��� �޼ҵ常 �ʿ��ϹǷ� Adapter Class�� ����Ͽ��� 						 */
	class Showtable {
		
		DefaultTableModel		datamodel;
		JTable					table;
		JScrollPane				scroll;
		
		String[] colName = {"��ȣ", "�̸�", "�ڵ�����ȣ", "E-Mail", "�ֹε�Ϲ�ȣ", 
							"����", "����", "�������", "����", "����"};
		
		//[�߿�] JTable�� �ִ� 2���� ����Ÿ���� ó���ϱ� ���� 2���� Vector���  
		Vector<Vector<String>> 	data;
		Vector<String>	 columnNames;

		public Showtable() {
			data = new Vector<Vector<String>>();  // �⺻10���� �����迭 ��ü����
			columnNames = new Vector<String>();
			
			//DefaultTableModel������ ���� vectorŸ������ ��ȯ
			for(int i = 0; i < colName.length; i++) {
				columnNames.add(colName[i]);
			}
			
			//JTable�� �ڷḦ ������ Object�� TableModel, Vector�� �Ķ���Ͱ����� ����
			//������ ����,����,������ ���� Model���¸� ����ؾ� �� (�ٸ� Ÿ���� ��¸� ����)
			//DefaultTableModel(Vector data, Vector ColumnNames) �����ڻ��
			datamodel = new DefaultTableModel(data, columnNames);
			//2�ܰ� table�� �ֱ�
			table = new JTable(datamodel);
			//3�ܰ� ��ũ���г� ���̱�
			scroll = new JScrollPane(table);
			
			//�� ���� ũ�� ����,  �� ����Ÿ ����,  �÷���ư ����
			setTable();
			scroll.getViewport().setBackground(Color.WHITE);

			//�̺�Ʈ ����
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					updateRow = table.getSelectedRow();
					//���õȿ��� �ప�� �����ͼ� �Է�TextField�� �ֱ�
					for(int i = 0; i < table.getColumnCount(); i++) {
						if(i < 5) 				west.input.tf[i].setText((String)table.getValueAt(updateRow, i));
						if(i >= 5 && i <= 8 )   west.output.lbRight[i-5].setText((String) table.getValueAt(updateRow, i));
						if(i == 9) 				west.input.cb.setSelectedItem((String)table.getValueAt(updateRow, i));	 
					}
					
					// ��Ȱ��ȭ
					west.input.tf[4].setEnabled(false);	// �ֹι�ȣ�Է�
					west.input.juminBtn.setEnabled(false);	// �ֹι�ȣ �ߺ��˻�
					buttons.addBtn.setEnabled(false);	// �߰���ư
				}
			});
		
			
						
		}
		///////   ���̺� ���� �� �÷��� Sort���
		private void setTable() {
			table.getColumnModel().getColumn(0).setPreferredWidth(30);
			table.getColumnModel().getColumn(1).setPreferredWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(120);
			table.getColumnModel().getColumn(3).setPreferredWidth(120);
			table.getColumnModel().getColumn(4).setPreferredWidth(120);
			table.getColumnModel().getColumn(5).setPreferredWidth(30);
			table.getColumnModel().getColumn(6).setPreferredWidth(30);
			table.getColumnModel().getColumn(7).setPreferredWidth(70);
			table.getColumnModel().getColumn(8).setPreferredWidth(70);
			table.getColumnModel().getColumn(9).setPreferredWidth(50);
			
			// ���� ����Ÿ�� ��� ���Ľ�Ű��
			DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
			cell.setHorizontalAlignment(SwingConstants.CENTER);
			
			for(int i=0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(cell);
			}
			
			//�÷�����ư ������  �����ϱ� 
//			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel()); 
//			table.setRowSorter(sorter); 
//			List<RowSorter.SortKey> sortKeys = new ArrayList<>(10); 
//			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); //SortKey(int column, SortOrder sortOrder) 
//			sorter.setSortKeys(sortKeys);
		}
	}
	
	/*			JTable ���	 (����Ŭ����) 		*/
	class Buttons extends JPanel {
		Vector<String> vector;
		JButton addBtn, updateBtn, delBtn, lastBtn, nextBtn, searchBtn, initBtn;
		String juminNo;
		
		public Buttons() {
			//FlowLayout ��ġ 
			//setLayout(new FlowLayout(3, 10, 10));
			setLayout(new GridLayout(1, 6));
			//��ư����
			addBtn = new JButton("�߰�");	 			delBtn = new JButton("����");
			lastBtn = new JButton("����");				nextBtn = new JButton("����");
			updateBtn = new JButton("����");				searchBtn = new JButton("�˻�");
			initBtn = new JButton("�ʱ�ȭ");
			
			//��ư������
			addBtn.setBackground(Color.ORANGE);			delBtn.setBackground(Color.GRAY);
			lastBtn.setBackground(Color.CYAN);			nextBtn.setBackground(Color.CYAN);
			updateBtn.setBackground(Color.PINK);		searchBtn.setBackground(Color.GREEN);
			
			// JPanel�� ��ư �߰�
			add(addBtn);	add(delBtn);	add(lastBtn);	add(nextBtn);	add(updateBtn);		add(searchBtn);	add(initBtn);

			//////        �߰���ư �̺�Ʈ ����          ////////////
			addBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					vector = new Vector<String>();
					
					//�Է� üũ ���� ����� �޼��� 
					boolean checkInput = validCheckInput();
					if(checkInput == false) return;
					
					//�ڵ�����ȣ �Է°� �˻�
					String hpNum = west.input.tf[2].getText();		//�ڵ�����ȣ�ʵ尪
					String hpnum_pattern = "^[0-9]{3}-[0-9]{4}-[0-9]{4}$";
					boolean checkHP = hpNum.matches(hpnum_pattern);
					if(checkHP == false) {
						JOptionPane.showMessageDialog(null, "�ڵ��� �Է��� �ٽ� Ȯ�����ּ���", "�ڵ��� �Է¿���", JOptionPane.ERROR_MESSAGE);
						west.input.tf[2].requestFocus();
						return;
					}
					//�̸��� �Է°� �˻�
					String email = west.input.tf[3].getText();		//�̸����ʵ尪
					String email_pattern = "^\\w+(\\.)?\\w+@\\w+\\.\\w+(\\.\\w+)?";
					boolean checkEmail = email.matches(email_pattern);
					if(checkEmail == false) {
						JOptionPane.showMessageDialog(null, "�̸��� �Է��� �ٽ� Ȯ�����ּ���", "�̸��� �Է¿���", JOptionPane.ERROR_MESSAGE);
						west.input.tf[3].requestFocus();
						return;
					}
					
					if(juminDoubleCheck == 0) {
						JOptionPane.showMessageDialog(null, "����� �ֹε�� �ߺ��˻縦 ���� �������ּ���", "�ֹε�Ϲ�ȣ �ߺ��˻� ��û", JOptionPane.ERROR_MESSAGE);
						return;
					}
						
					
					// �ֹι�ȣ �˻� �� ����, ����, �������, ���� ���� 
					String juminNum = west.input.tf[4].getText();	//�ֹι�ȣ�ʵ尪
					/**
					 * �Էµ� �ֹι�ȣ ��ȿ�� �˻�
					 * �Է°� �˻�  (����ǥ��������) ^[0-9]{6}-[1-4][0-9]{6}$ ����: ^ ��:$
					 */
					String regex = "^[0-9]{6}-[1-4][0-9]{6}$";
					Boolean check = juminNum.matches(regex);
						
					if(check == false) {
						JOptionPane.showMessageDialog(null, "�ֹι�ȣ�� �ùٸ��� �ʽ��ϴ�", "�ֹι�ȣ �Է� ����", JOptionPane.ERROR_MESSAGE);
						west.input.tf[4].setText(null);
						west.input.tf[4].requestFocus();
						return;	//�� ���� ����
					}
					// �ֹι�ȣ ��ȿ��üũ
					Boolean validJumin = calJuminCode(juminNum);
					if(validJumin == false) return;
					
					// ���̰��
					String age = calAge(juminNum);
					// ����üũ
					String gender="";
					if (juminNum.charAt(7)-'0' == 1 || juminNum.charAt(7)-'0' == 3) gender = "����";
					else gender = "����";
					// ����������
					String location = getLocation(juminNum);
					// ���ϰ��
					String birthday = getBirthDay(juminNum);
					
					//�Ż����� ��¿� �迭
					String[] info = {age, gender, location, birthday};
					//�Ż����� Label�� ���
					for(int i = 0; i < info.length; i++) 
						west.output.lbRight[i].setText(info[i]);
					//west�� �ִ� �Է��ʵ忡�� �� �����ͼ� ���͹迭�� �߰�
					for(int j = 0; j < showtable.table.getColumnCount(); j++) {
						if(j <= 4)			vector.add(west.input.tf[j].getText()); 
						if(j >= 5 && j < 9)  vector.add(info[j-5]); 
						if(j == 9) 			vector.add((String) west.input.cb.getSelectedItem());	// ����
					}
					//showtableŬ������ �ִ� 2���� Vector<Vector<String>> data�� �߰� 
					showtable.data.addElement(vector);
					
					//�߿� fileTableDataChanged()�޼ҵ带 ȣ���ϸ�
					//JTable�� �����Ͱ� ����Ǿ����� �����ʿ��� ������. 
					showtable.datamodel.fireTableDataChanged();
					
					initTextField(); //�Է��ʵ��ʱ�ȭ �ż��� ȣ��
					juminDoubleCheck = 0;
					JOptionPane.showMessageDialog(null, "������� ���������� �Ǿ����ϴ�.", "����ϿϷ�", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			/////////////////    ������ư �̺�Ʈ ����   //////////////////
			updateBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// JTable�� ���õ� row ���� ��ȯ 
					updateRow = showtable.table.getSelectedRow(); 
					//System.out.println("updateRow" + updateRow); 
					if(updateRow == -1) { 
						JOptionPane.showMessageDialog(null, "������ ȸ�������� ���õ��� �ʾҽ��ϴ�", "���� ���ÿ� ����", JOptionPane.ERROR_MESSAGE);		 
						return; 
					} 
					//�Է� üũ ���� ����� �޼��� 
					boolean checkInput = validCheckInput();
					if(checkInput == false) 	return;
					
					// JTable�� �����͸� �������ִ� Method
					//-> setValueAt(data, row, column);
					showtable.table.setValueAt(west.input.tf[0].getText(), updateRow, 0);
					showtable.table.setValueAt(west.input.tf[1].getText(), updateRow, 1);
					showtable.table.setValueAt(west.input.tf[2].getText(), updateRow, 2);
					showtable.table.setValueAt(west.input.tf[3].getText(), updateRow, 3);
					showtable.table.setValueAt(west.input.tf[4].getText(), updateRow, 4);
					showtable.table.setValueAt(west.input.cb.getSelectedItem(), updateRow, 9);
					
					//�ֹι�ȣ�� ��������
					//west.input.tf[4].setEnabled(true);  //�ֹι�ȣ �Է� ���
					showtable.table.removeRowSelectionInterval(0, updateRow); // ���̺� ���ð�����
					initTextField();	// �Է��ʵ� �ʱ�ȭ
					JOptionPane.showMessageDialog(null, "����ϼ����� ���������� ó�� �Ǿ����ϴ�.", "�������Ϸ�", JOptionPane.INFORMATION_MESSAGE);

				}
			});
			/////////////////    ������ư �̺�Ʈ ����   //////////////////
			delBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int deleteRow = showtable.table.getSelectedRow();
					
					if(deleteRow == -1) {	
						JOptionPane.showMessageDialog(null, "������ ȸ�������� ���õ��� �ʾҽ��ϴ�", "���� ���ÿ� ����", JOptionPane.ERROR_MESSAGE);		
						return;
					}
					int select = JOptionPane.showConfirmDialog(null, "������ ȸ���� ���� �����Ͻðڽ��ϱ�?", "���� Ȯ�� �޽���", JOptionPane.YES_NO_OPTION);
					if(select == JOptionPane.YES_OPTION) {
						//���̺��� ���� �������� �޼ҵ�
						//showtable.datamodel = showtable.table.getModel();
						//�ش��ϴ� ��ġ�� ���� ����
						showtable.datamodel.removeRow(deleteRow);
						initTextField();
						JOptionPane.showMessageDialog(null, "������ ������ ���������� �Ǿ����ϴ�.", "������ �����Ϸ�", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}
			});
			//////////  �˻� ��ư �̺�Ʈ //////////////
			searchBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					west.output.card.next(west.output);
					searchBtn.setEnabled(false);
					initTextField();
					west.output.searchRadio[0].setSelected(true);
					
				}
			});
			//////      ������ư �̺�Ʈ ����          ////////////
			lastBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					updateRow = updateRow - 1;	//���õȿ��� ������
					
					//System.out.println("������ư�̺�Ʈ ���� ����� Row�� : " + updateRow);
					if(updateRow < 0) {
						JOptionPane.showMessageDialog(null, "���̻� ����� ����Ÿ�� �����ϴ�", "��� ���� �޽���", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					for(int i = 0; i < showtable.table.getColumnCount(); i++) {
						if(i < 5) 				west.input.tf[i].setText((String)showtable.table.getValueAt(updateRow, i));
						if(i >= 5 && i <= 8 )   west.output.lbRight[i-5].setText((String)showtable.table.getValueAt(updateRow, i));
						if(i == 9) 				west.input.cb.setSelectedItem((String)showtable.table.getValueAt(updateRow, i));	 
					}
					//�ֹι�ȣ ���� ��Ȱ��ȭ
					west.input.tf[4].setEnabled(false);
				}
			});
			//////      ������ư �̺�Ʈ ����          ////////////
			nextBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					updateRow = updateRow + 1;	//���õ����� ������
					int lastRow = showtable.table.getRowCount() - 1; //������ �ప
					//System.out.println("������ư�̺�Ʈ ���� ����� Row�� : " + updateRow);
					if(updateRow > lastRow) {
						JOptionPane.showMessageDialog(null, "���̻� ����� ����Ÿ�� �����ϴ�", "��� ���� �޽���", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					for(int i = 0; i < showtable.table.getColumnCount(); i++) {
						if(i < 5) 				west.input.tf[i].setText((String)showtable.table.getValueAt(updateRow, i));
						if(i >= 5 && i <= 8 )   west.output.lbRight[i-5].setText((String)showtable.table.getValueAt(updateRow, i));
						if(i == 9) 				west.input.cb.setSelectedItem((String)showtable.table.getValueAt(updateRow, i));	 
					}
					//�ֹι�ȣ ���� ��Ȱ��ȭ
					west.input.tf[4].setEnabled(false);
				}
			});
			//////  �ʱ�ȭ ��ư �̺�Ʈ ����          ////////////
			initBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					initTextField();
					//west.input.juminBtn.setEnabled(true);
					west.output.lbRight[0].setText(null);
					west.output.lbRight[1].setText(null);
					west.output.lbRight[2].setText(null);
					west.output.lbRight[3].setText(null);
					buttons.addBtn.setEnabled(true);
					if(showtable.table.getSelectedRow() >= 0) {
						showtable.table.removeRowSelectionInterval(0, updateRow); // ���̺� ���ð�����
					}
				}
			});
			
		}
		
		
		////// �Է°� üũ �ż��� ////////
		private boolean validCheckInput() {
			if(west.input.tf[0].getText().trim().isEmpty()) { 
				JOptionPane.showMessageDialog(null, "��ȣ�� �Է��� �ȵǾ����ϴ�", "�Է� ����", JOptionPane.ERROR_MESSAGE);
				west.input.tf[0].requestFocus();
				return false;
			}
			if(west.input.tf[1].getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "�̸��� �Է��� �ȵǾ����ϴ�", "�Է� ����", JOptionPane.ERROR_MESSAGE);
				west.input.tf[1].requestFocus();
				return false;
			}
			if(west.input.tf[2].getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "�ڵ�����ȣ�� �Է��� �ȵǾ����ϴ�", "�Է� ����", JOptionPane.ERROR_MESSAGE);
				west.input.tf[2].requestFocus();
				return false;
			}
			//�ڵ�����ȣ �Է°� �˻�
			String hpNum = west.input.tf[2].getText();		//�ڵ�����ȣ�ʵ尪
			String hpnum_pattern = "^[0-9]{3}-[0-9]{4}-[0-9]{4}$";
			boolean checkHP = hpNum.matches(hpnum_pattern);
			if(checkHP == false) {
				JOptionPane.showMessageDialog(null, "�ڵ��� �Է��� �ٽ� Ȯ�����ּ���", "�ڵ��� �Է¿���", JOptionPane.ERROR_MESSAGE);
				west.input.tf[2].requestFocus();
				return false;
			}
			//�̸��� �Է°� �˻�
			String email = west.input.tf[3].getText();		//�̸����ʵ尪
			String email_pattern = "^\\w+(\\.)?\\w+@\\w+\\.\\w+(\\.\\w+)?";
			boolean checkEmail = email.matches(email_pattern);
			if(checkEmail == false) {
				JOptionPane.showMessageDialog(null, "�̸��� �Է��� �ٽ� Ȯ�����ּ���", "�̸��� �Է¿���", JOptionPane.ERROR_MESSAGE);
				west.input.tf[3].requestFocus();
				return false;
			}
			return true;
		}
		////// �Է°� �ʱ�ȭ �ż��� ////////
		public void initTextField() {
			//�Է��ʵ� ���� ��Ŀ���̵�
			west.input.tf[0].setText(null);
			west.input.tf[1].setText(null);
			west.input.tf[2].setText(null);
			west.input.tf[3].setText(null);
			west.input.tf[4].setText(null);
			west.input.juminBtn.setEnabled(true);
			west.input.cb.setSelectedIndex(0);
			west.input.tf[0].requestFocus();
			west.input.tf[4].setEnabled(true);
			buttons.addBtn.setEnabled(true);
		}
		////// ���̰�� �ż��� ////////////////
		public String calAge(String juminNum) {
			/**
			 * 1.����⵵�� �ý������κ��� ���� Calender Ŭ���� ���� 2. �ֹι�ȣ ���ڸ��� ������Ͽ��� ���ڸ��� ��������"97" -> ������
			 * ��ȯ StringŬ������ substring()�޼ҵ� ���� 3. ��7�ڸ��� ù��° �ڸ� 1,2,3,4 �� ��������� ���Ǵ��Ͽ� if 1,2
			 * 1900�� �����ְ� if 3,4 -> 2000�� �����ش� 4. (2021-1997)+1 = ����
			 */
			Calendar cal = Calendar.getInstance(Locale.KOREA);
			int nowYear = cal.get(Calendar.YEAR); // �ý��� ����
			int myYear = Integer.parseInt(juminNum.substring(0, 2)); // �� �ֹι�ȣ ����

			int genderCode = (juminNum.charAt(7) - '0'); // �� �����ڸ�
			if (genderCode == 1 || genderCode == 2)
				myYear += 1900;
			if (genderCode == 3 || genderCode == 4)
				myYear += 2000;
			int age = (nowYear - myYear) + 1;
			return String.valueOf(age);
		}
		////// �ֹι�ȣ ��ȿ�� üũ �ż���  ////////////
		public Boolean calJuminCode(String juminNum) {
			// �ֹι�ȣ ��ȿ�� üũ
			int[] weight = { 2, 3, 4, 5, 6, 7, 0, 8, 9, 2, 3, 4, 5 }; // �������� ����ġ��
			int sum = 0;
			int[] code = new int[2];
			for (int i = 0; i < 13; i++) {
				if (juminNum.charAt(i) != '-') {
					sum += (juminNum.charAt(i) - '0') * weight[i];
				}
			}
			int sysCode = 11 - (sum % 11);
			code[0] = sysCode % 10;
			code[1] = juminNum.charAt(13) - '0'; // �ֹι�ȣ ������ �ڸ���
			
			if(code[0] != code[1]) {
				JOptionPane.showMessageDialog(null, "��ȿ�� �ֹε�Ϲ�ȣ�� �Էµ��� �ʾҽ��ϴ�", "�ֹι�ȣ ��ȿ���˻� ����", JOptionPane.ERROR_MESSAGE);
				west.input.tf[4].setEnabled(true);
				west.input.juminBtn.setEnabled(true);
				west.input.tf[4].requestFocus();
				juminDoubleCheck = 0;
				return false;	
			}
			return true;
		}
		//// �������� ���� �ż���   //////////
		public String getLocation(String juminNum) {
			/*
			 * ��������  -> �ֹι�ȣ ���ڸ��� 2~3��° �ڸ� �� ��� 
			 */
			int myLocationCode = Integer.parseInt(juminNum.substring(8, 10));
			String[][] arrLocation = {{"����","00","08"}, {"�λ�","09","12"},{"��õ","13","15"},{"���","16","25"},
						{"����","26","34"}, {"���","35","39"},{"����","40","40"},{"�泲","41","43"},
						{"�泲","45","47"}, {"����","44","44"},{"����","96","96"},{"����","48","54"},
						{"����","55","64"}, {"����","65","66"},{"�뱸","67","70"},{"���","71","80"},
						{"�泲","81","84"}, {"�泲","86","90"},{"���","85","85"},{"����","91","95"}};
			String location = null;
			for(int i=0; i < arrLocation.length; i++) {
				int start = Integer.parseInt(arrLocation[i][1]); 
				int end = Integer.parseInt(arrLocation[i][2]);
				if(myLocationCode >= start && myLocationCode <= end)	location = arrLocation[i][0]; 
			}
			return location;
		}
		/////  ������� ��� �ż��� ////////
		public String getBirthDay(String juminNum) {
			int myYear = Integer.parseInt(juminNum.substring(0, 2)); // �� �ֹι�ȣ ����
			int genderCode = (juminNum.charAt(7) - '0'); 
			if (genderCode == 1 || genderCode == 2)	myYear += 1900;
			if (genderCode == 3 || genderCode == 4)	myYear += 2000;
			String birthday = String.valueOf(myYear) + juminNum.substring(2, 6);
			return birthday;
		}
	}
	
	//���� ���α׷����� ��â
	class newWindow extends JFrame { 
	
		JTextArea ta = new JTextArea(7, 20);
		
		public newWindow() {
			// ����, ���⼭ setDefaultCloseOperation() ���Ǹ� ���� ���ƾ� �Ѵ�
	        // �����ϰ� �Ǹ� �� â�� ������ ��� â�� ���α׷��� ���ÿ� ������
	        setTitle("���α׷�����");
	        Container c = getContentPane();
			c.setLayout(new FlowLayout());
	        String str = "������ �ý��� \n \n" + 
	        		"�ֿ��� : ������ �߰� �� ����, ���� \n" +
	        		"         ���Ͽ��� �� ����  \n"+ 
	        		"         �˻� �� ���� ��� \n \n" +
	        		"  <<�����̷�(Modificatoin Information)>>  \n\n" + 
	        		"  ������           ������		��������\r\n" + 
	        		"  ___________________________________________\r\n" + 
	        		"   2021.04.08  �ּ���     ȭ�鱸��, ������ �߰�,����,�����̺�Ʈó��\r\n" + 
	        		"   2021.04.09  �ּ���     ���Ͽ��� �� ����, �Ż����� ��� \r\n" + 
	        		"   2021.04.10  �ּ���     ������ �˻� �� ���ı��";
	        
	        ta.setText(str);
	        c.add(ta);
	        
	        setSize(400,300);
	        setLocation(500,300);
	        setLocationRelativeTo(null);//â�� ��� ������
	        setResizable(true);
	        setBackground(Color.white);
	        setVisible(true);
		}
	}
	
	public static void main(String[] args) {
		new CustomerSystem();
	}
}

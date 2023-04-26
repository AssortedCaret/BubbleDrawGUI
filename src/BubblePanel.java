import javax.swing.JPanel;
import java.awt.Color;
import java.util.Random;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.EventListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class BubblePanel extends JPanel {
	Random rand = new Random();
	ArrayList<Bubble> bubbleList;
	int size = 25;
	Timer timer;
	int delay = 33;
	JSlider slider;
	// объединяет все классы и методы для работы
	public BubblePanel() {
		timer = new Timer(delay, new BubbleListener());
		bubbleList = new ArrayList<Bubble>();
		setBackground(Color.BLACK);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 450, 33);
		add(panel);
		panel.setLayout(null);

		JButton btnButtonPause = new JButton("Pause");
		btnButtonPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				if (btn.getText().equals("Pause")) {
					timer.stop();
					btn.setText("Start");
				}
				else {
					timer.start();
					btn.setText("Pause");
				}
			}
		});
		
		btnButtonPause.setBounds(295, 7, 71, 18);
		panel.add(btnButtonPause);

		JButton btnButtonClear = new JButton("Clear");
		btnButtonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bubbleList = new ArrayList<Bubble>();
				repaint();
			}
		});
		
		btnButtonClear.setBounds(376, 7, 64, 18);
		panel.add(btnButtonClear);
		
		JLabel lblLabelText = new JLabel("Animation Speed");
		lblLabelText.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLabelText.setBounds(10, 9, 102, 14);
		panel.add(lblLabelText);
		
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
			int speed = slider.getValue() + 1;
			int delay = 1000 / speed;
			timer.setDelay(delay);
			}
		});
		
		slider.setValue(30);
		slider.setMajorTickSpacing(30);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(5);
		slider.setMaximum(120);
		slider.setBounds(122, 7, 159, 15);
		panel.add(slider);

		//testBubble();
		addMouseListener(new BubbleListener());
		addMouseMotionListener(new BubbleListener());
		addMouseWheelListener(new BubbleListener());
		timer.start();
	}
	
	//прописан метод рисования пузырьков
	public void paintComponent (Graphics canvas) {
		super.paintComponent(canvas);
		//конструкция фор ~ для каждого объекта б типа Бабл в массиве бабл лист {объект б рисуется на холсте}
		for(Bubble b: bubbleList) {
			b.draw(canvas);
		}
	}
	
	// тест пузырьков (отключен)
	public void testBubble() {
		for(int n = 0; n<100; n++) {
			int x = rand.nextInt(600);
			int y = rand.nextInt(400);
			int size = rand.nextInt(50);
			bubbleList.add(new Bubble(x, y, size));
		}	
		repaint();
	}
	
	//класс отвечающий за создание пузырьков с помощью мыши
	private class BubbleListener extends MouseAdapter implements ActionListener {
		public void mousePressed (MouseEvent e) {
			bubbleList.add(new Bubble (e.getX(), e.getY(), size));
			repaint();
		}
		
		public void mouseDragged(MouseEvent e) {
			bubbleList.add(new Bubble(e.getX(), e.getY(), size));
			repaint();
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			//для одинаковой работы прокрутки колесика для разных систем
			if(System.getProperty("os.name").startsWith("MAC"))
				size += e.getUnitsToScroll();
			else
				size -= e.getUnitsToScroll();

			if(size < 3) {
				size = 3;	
			}
		}
		
		public void actionPerformed (ActionEvent e) {
			for (Bubble b: bubbleList)
				b.update();
			repaint();
		}
	}
	
	//класс отвечающий за создание пузырьков
	private class Bubble {
		private int x;
		private int y;
		private int size;
		private Color color;
		private int xspeed, yspeed;
		private final int MAX_SPEED = 5;

		public Bubble (int newX, int newY, int newSize) {
			x = newX;
			y = newY;
			size = newSize;
			color = new Color(rand.nextInt(256),
					rand.nextInt(256),
					rand.nextInt(256),
					rand.nextInt(256));
			xspeed = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
			yspeed = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
			if (xspeed == 0 || yspeed ==0) {
				xspeed =2;
				yspeed =2;
			}
		}

		public void draw (Graphics canvas) {
			canvas.setColor(color);
			canvas.fillOval(x - size/2, y - size/2, size, size);
		}
		
		public void update() {
			y += yspeed;
			x += xspeed;
			if (x-size/2 <=0 || x+size/2>=getWidth()) 
				xspeed = -xspeed;
			if (y-size/2<=0 || y+size/2>=getHeight())
				yspeed = -yspeed;
		}
	}
}


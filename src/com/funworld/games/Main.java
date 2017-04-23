package com.funworld.games;

public class Main {
	public static void main(String[] args){
		LoginFrame loginFrame = new LoginFrame(new CredentialsHandler() {
			
			public void submit(User user) {
				MainFrame mainFrame = new MainFrame(user);
				mainFrame.start();
			}
		});
	}
}

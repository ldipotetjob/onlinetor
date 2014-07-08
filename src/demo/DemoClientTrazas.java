/*
 * Copyright (c) 2010 Matthew J. Francis and Contributors of the Bobbin Project
 * This file is distributed under the MIT licence. See the LICENCE file for further information.
 */
package demo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.itadaki.bobbin.bencode.InvalidEncodingException;
import org.itadaki.bobbin.peer.Peer;
import org.itadaki.bobbin.peer.TorrentManager;
import org.itadaki.bobbin.peer.TorrentSetController;
import org.itadaki.bobbin.torrentdb.IncompatibleLocationException;
import org.itadaki.bobbin.trackerclient.TrackerClientStatus;



/**
 * A demonstration BitTorrent client
 */
public class DemoClientTrazas {

	/**
	 * The controller for the torrents being downloaded
	 */
	private static TorrentSetController controller;

	/**
	 * Prints a program usage message
	 */
	private static void usage() {

		System.out.println ("Usage: democlient <torrent file>");

	}


	/**
	 * Exits forcefully, printing a message to stderr
	 *
	 * @param message The message to display
	 */
	private static void exitWithError (String message) {

		System.err.println (message);
		System.exit (0);

	}

	static TorrentManager manager = null;
	/**
	 * Main method
	 *
	 * @param arguments ignores
	 */
	public static void main (String[] arguments) {
		test();
	}
	
	public static void test () {

	/*	if (arguments.length == 0) {

			usage();

		} else {*/

		/*	if (arguments.length != 1) {
				System.err.println ("Error: Missing argument");
				usage();
				System.exit (0);
			}*/

			String torrentFilename = "E:/proyectos/torrent/Esto-no-es-una-cita-DVDRip(EliteTorrent).torrent";
			File torrentFile = new File (torrentFilename);

			File metadataDirectory = new File (new File (System.getProperty ("user.home")), ".democlient" + File.separator + "metadata");
			try {
				controller = new TorrentSetController (metadataDirectory);
			} catch (IOException e) {
				e.printStackTrace ();
				exitWithError ("Could not open server socket");
			}

			// Can't actually happen. Satisfies compiler stupidity
			if (controller == null) return;

			
			try {
				manager = controller.addTorrentManager (torrentFile, new File("."));
				manager.start (false);
			} catch (InvalidEncodingException e) {
				exitWithError ("Invalid torrent file");
			} catch (IncompatibleLocationException e) {
				exitWithError ("Incompatible location or filename");
			} catch (IOException e) {
				exitWithError ("Could not read torrent file");
			}

			// Can't actually happen. Satisfies compiler stupidity
			if (manager == null) return;

			/*TorrentWindow torrentWindow = new TorrentWindow (torrentFile.getName(), manager);
			torrentWindow.addWindowListener (new WindowAdapter() {
				@Override
				public void windowClosing (WindowEvent e) {
					controller.terminate (true);
					System.exit (0);
				}
			});
			torrentWindow.setVisible (true);*/

			manager.start (false);
			
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					final int bytesSentPerSecond = manager.getProtocolBytesSentPerSecond();
					final int bytesReceivedPerSecond = manager.getProtocolBytesReceivedPerSecond();
					final TrackerClientStatus trackerClientStatus = manager.getTrackerClientStatus();
					final int verifiedPieceCount = manager.getVerifiedPieceCount();
					final int totalPieces = manager.getNumberOfPieces();
					final int numPresentPieces = manager.getPresentPieces().cardinality();
					final Set<Peer> peers = manager.getPeers();

					SwingUtilities.invokeLater (new Runnable() {
						public void run() {
							System.out.println(String.format ("Sent: %1.2f KB/s", (float)bytesSentPerSecond / 1000));
							System.out.println(String.format ("Received: %1.2f KB/s", (float)bytesReceivedPerSecond / 1000));
							Long timeOfLastTrackerUpdate = trackerClientStatus.getTimeOfLastUpdate();
							String trackerFailureReason = trackerClientStatus.getFailureReason();
							Integer timeUntilNextTrackerUpdate = trackerClientStatus.getTimeUntilNextUpdate ();
							String lastAnnounceTime = (timeOfLastTrackerUpdate == null) ? "N/A" : new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format (new Date (timeOfLastTrackerUpdate));
							String trackerResponse = (timeOfLastTrackerUpdate == null) ? "N/A" : (trackerFailureReason == null) ? "Success" : trackerFailureReason;
							System.out.println( "Next tracker update: " + (
									trackerClientStatus.isUpdating() ? "In progress" : 
										((timeUntilNextTrackerUpdate != null) ? timeUntilNextTrackerUpdate : "N/A")
									));
							System.out.println("Last Announce: " + lastAnnounceTime);
							System.out.println("Tracker Response: " + trackerResponse);
							System.out.println("Verified: " + verifiedPieceCount + " / " + totalPieces);
							float percentComplete = (totalPieces == 0) ? 0 : ((float) (100 * numPresentPieces) / totalPieces);
							System.out.println (String.format ("Complete: %d of %d pieces (%1.2f%%)", numPresentPieces, totalPieces, percentComplete));
							//peerTableModel.updatePeerSet (peers);
						}
					});
				}

			};
			new Timer().schedule (task, 1000, 1000);

		}

	//}


}

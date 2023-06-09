package DAO;

import Table.*;


public interface ParticipantDAO {
	public Participants getParticipantByKey(int regs_id);

	public void addParticipant(Participants participant) throws Exception;

	public void updateParticipant(Participants participants) throws Exception;

	public void deleteParticipant(Participants participant);

	public int getNextID();

}

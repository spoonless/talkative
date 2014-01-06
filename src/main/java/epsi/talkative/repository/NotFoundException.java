package epsi.talkative.repository;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

}

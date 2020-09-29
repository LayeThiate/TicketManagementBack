#!/bin/bash

echo "Setup in progress..."

if dpkg -s postgresql postgresql-client >/dev/null 2>/dev/null; then
	echo "PostgreSQL is already installed..."
else
	echo "Installing PostgreSQL..."
	apt-get install postgresql postgresql-client
	while ! dpkg -s postgresql postgresql-client >/dev/null 2>/dev/null; do
		echo -n "PostgreSQL installation failed... Retry? [Y/n] "
		read -r yesorno
		case "$yesorno" in
			Y | y | "\n" ) apt-get install postgresql postgresql-client;;
			* ) exit;;
		esac
	done
	echo "PostgreSQL installation successed!"
fi

"$(dirname "$0")/restart.sh"

echo "Please enter new password for user postgres in PostgreSQL (please choose 'postgres'):"
sudo -u postgres psql -c "\password postgres"

sudo -u postgres psql -f "$(dirname "$0")/create_database.sql"

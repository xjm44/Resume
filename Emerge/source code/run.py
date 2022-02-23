from flask import Flask
from controller import *

# Starting the python applicaiton
if __name__ == '__main__':
    # Step 1: Change this port number if needed
    PORT_NUMBER = 8080
    host = '0.0.0.0'

    print("-"*80)
    print("""Welcome to Server Backend.\n
             Please open your browser to:
             http://0.0.0.0:{}""".format(PORT_NUMBER))
    print("-"*80)
    # Note, you're going to have to change the PORT number
    app.run(debug=True, host=host, port=PORT_NUMBER)

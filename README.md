# imagelocation

Provides a web interface and command line interface to process image files and extract their GPS information.


## Usage

```
usage: imagelocation.jar [options] [filename[s]]
options:
  -w, --web   Start web app
  -h, --help  Show help usage

Examples:
java -jar imagelocation.jar -w           ;; start web app
java -jar imagelocation.jar photo.jpg    ;; process single file
java -jar imagelocation.jar *.jpg        ;; process all jpg files

Output from command line processing:
filename,lat,lng,link
```


## License

Copyright © 2019 Brad Lucas, brad@beaconhill.com

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

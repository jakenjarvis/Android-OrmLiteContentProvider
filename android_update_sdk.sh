#!/bin/bash
# This file is part of the Android-OrmLiteContentProvider package.
#
# Copyright (c) 2012, Android-OrmLiteContentProvider Team.
#					  Jaken Jarvis (jaken.jarvis@gmail.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#	 http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# The author may be contacted via
# https://github.com/jakenjarvis/Android-OrmLiteContentProvider

PARAM=$*

if ! type expect >/dev/null 2>&1; then
	echo "Install expect"
	sudo apt-get install expect -y -qq
	expect -v
fi

expect - <<EOF
	set timeout 1800
	spawn android update sdk $PARAM
	expect {
		"Do you accept the license *:" {
			send "y\n"
			exp_continue
		}
		"Done. * packages installed." {
			exit
		}
	}
EOF

echo $PARAM | grep -o -e build-tools-[0-9.]* | grep -o -e [0-9.]* | while read android_build_tools_version
do
	echo "--------------------------------------------------"
	echo "Check build-tools $android_build_tools_version"
	echo "--------------------------------------------------"
	if [ ! -n "$ANDROID_HOME" ]; then
		echo "ANDROID_HOME has not been set."
		exit 1
	fi

	mkdir -p $ANDROID_HOME/build-tools/

	target_dir=${ANDROID_HOME}/build-tools/$android_build_tools_version
	echo "target : ${target_dir}"

	if [ ! -e $target_dir/aidl ]; then
		echo "Install build-tools $android_build_tools_version"

		version_array=(`echo $android_build_tools_version | tr -s '.' ' '`)
		major=${version_array[0]}
		minor=${version_array[1]}
		micro=${version_array[2]}

		# TODO: This is too simple?
		target_zip_array=("${major}.${minor}.${micro}" "${major}.${minor}" "${major}" "$android_build_tools_version")
		for i in "${target_zip_array[@]}"
		do
			target_zip=build-tools_r${i}-linux.zip
			echo "Search ${target_zip}"
			result=`wget -nv --spider https://dl-ssl.google.com/android/repository/${target_zip} >/dev/null 2>&1; echo "$?"`
			if [ $result -eq "0" ]; then
				echo "${target_zip} was found."
				break
			fi
		done

		wget https://dl-ssl.google.com/android/repository/${target_zip} > /dev/null

		if [ -e $target_zip ]; then
			android_os_version=`unzip -l ${target_zip} | grep android- | head -1 | grep -o -e android-[0-9.]*`

			unzip $target_zip -d $ANDROID_HOME/build-tools/ > /dev/null
			mv $ANDROID_HOME/build-tools/$android_os_version $target_dir

			echo "Download the ${target_zip} has been completed."
			ls -al ${target_dir}
		else
			echo "Failed to download the ${target_zip}."
			exit 1
		fi
	else
		echo "Existed build-tools $android_build_tools_version"
		ls -al ${target_dir}
	fi
done

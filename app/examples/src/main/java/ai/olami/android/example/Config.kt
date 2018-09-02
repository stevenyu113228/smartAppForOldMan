/*
	Copyright 2017, VIA Technologies, Inc. & OLAMI Team.

	http://olami.ai

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package ai.olami.android.example

import ai.olami.cloudService.APIConfiguration

object Config {

    // * Replace your APP KEY with this variable.
    var appKey = "bdcc99ccc57d48dfb4f154c5026e315f"

    // * Replace your APP SECRET with this variable.
    var appSecret = "94a6ee72f08c467f8abda8ced25c89cf"

    // * Replace the localize option you want with this variable.
    // * - Use LOCALIZE_OPTION_SIMPLIFIED_CHINESE for China
    // * - Use LOCALIZE_OPTION_TRADITIONAL_CHINESE for Taiwan
    //    private static int mLocalizeOption = APIConfiguration.LOCALIZE_OPTION_TRADITIONAL_CHINESE;
    var localizeOption = APIConfiguration.LOCALIZE_OPTION_SIMPLIFIED_CHINESE
    val myurl = "194e8462.ngrok.io" //change this


}

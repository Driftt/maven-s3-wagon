/**
 * Copyright 2010-2015 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.maven.wagon.auth;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.google.common.base.Optional;
import org.apache.maven.wagon.authentication.AuthenticationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * This chain searches for AWS credentials in system properties -> environment variables -> ~/.m2/settings.xml -> Amazon's EC2 Instance Metadata Service
 */
public final class MavenAwsCredentialsProviderChain extends AWSCredentialsProviderChain {

	public MavenAwsCredentialsProviderChain(Optional<AuthenticationInfo> auth) {
		super(getProviders(auth));
	}

	private static AWSCredentialsProvider[] getProviders(Optional<AuthenticationInfo> auth) {
		List<AWSCredentialsProvider> providers = new ArrayList<AWSCredentialsProvider>();

		// We want to get AWS credentials from the default providers if able,
		// And fall back to the local settings.xml otherwise
		// (See DefaultAWSCredentialsProviderChain javadoc for details)
		providers.add(new DefaultAWSCredentialsProviderChain());
		providers.add(new AuthenticationInfoCredentialsProvider(auth));

		return providers.toArray(new AWSCredentialsProvider[providers.size()]);
	}

}

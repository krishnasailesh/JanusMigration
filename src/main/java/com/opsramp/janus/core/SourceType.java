/*
 * This computer program is the confidential information and proprietary trade
 * secret of VistaraIT, Inc. Possessions and use of this program must
 * conform strictly to the license agreement between the user and
 * VistaraIT, Inc., and receipt or possession does not convey any rights
 * to divulge, reproduce, or allow others to use this program without specific
 * written authorization of VistaraIT, Inc.
 * 
 * Copyright  2014 VistaraIT, Inc. All Rights Reserved.
*/
package com.opsramp.janus.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Murthy Chelankuri
 * 
 */
public enum SourceType {
	
	DOCKER("Docker"),
	SQS("AWS/SQS"),
	DEVICE("Device"),
	LOAD_BALANCER("AWS/Load Balancer"),
	KVM("KVM"),
	EBS("AWS/Volume"),
	RDS("AWS/RDS"),
	DEVICE_GROUP("Device Group"),
	CLOUD_PROVIDER("Cloud Provider"),
	AUTO_SCALING("AWS/Auto Scaling"),
	ILB_ADDRESSPOOL("AZURE/ILB BackendAddressPool"),
	ILB_RULE("AZURE/ILB Rule"),
	ILB_IPCONFIG("AZURE/ILB FrontendIpConfiguration"),
	ILB_PROBE("AZURE/ILB Probe"),
	ILB_IB_NATRULE("AZURE/ILB InboundNatRule"),
	ECS_CLUSTER("AWS/ECS Cluster"),
	VPC("VPC"),
	SECURITY_GROUP("AWS/Security Group"),
	ECS_SERVICE("AWS/ECS Service");
	

	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private SourceType(String name) {
		this.name = name;
	}
		
}
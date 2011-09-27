package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.jira.RPCModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.RPCProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.6
 */
@ModuleCreatorClass(RPCModuleCreator.class)
public class RPCPrompter extends AbstractModulePrompter<RPCProperties>
{

    public static final List<String> RPC_ANSWERS = new ArrayList<String>(Arrays.asList("S", "s", "X", "x"));

    public RPCPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public RPCProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String soapOrXml = prompt("[S]OAP or [X]ML-RPC?", RPC_ANSWERS, "S");
        boolean isSoap = true;
        String rpcType = "Soap";

        if ("x".equals(soapOrXml.toLowerCase()))
        {
            isSoap = false;
            rpcType = "Xml";
        }

        String interfaceName = promptJavaClassname("Enter Interface name", "MY" + rpcType + "Endpoint");
        String interfacePackage = promptJavaPackagename("Enter Interface package", getDefaultBasePackage() + ".jira.rpc");

        String className = promptJavaClassname("Enter Class name", interfaceName + "Impl");
        String packageName = promptJavaPackagename("Enter Package Name", interfacePackage);

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);
        String fqInterface = ClassnameUtil.fullyQualifiedName(interfacePackage, interfaceName);

        RPCProperties props = new RPCProperties(fqInterface);
        props.setFullyQualifiedClassname(fqClass);
        props.setSoap(isSoap);

        props.setServicePath(promptNotBlank("Enter Service Path", props.getServicePath()));

        return props;
    }

    @Override
    public void promptForAdvancedProperties(RPCProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {

    }
}
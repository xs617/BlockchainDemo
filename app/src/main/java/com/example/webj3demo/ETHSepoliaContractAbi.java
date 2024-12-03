package com.example.webj3demo;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.contracts.token.ERC20Interface;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class ETHSepoliaContractAbi extends Contract implements ERC20Interface {
    public static final String BINARY = "608060405234801562000010575f80fd5b506040518060400160405280600981526020016811985cdd08109d5b1b60ba1b815250604051806040016040528060048152602001631095531360e21b8152508160039081620000619190620002c2565b506004620000708282620002c2565b505050620000aa3362000088620000b060201b60201c565b6200009590600a6200049d565b620000a490620186a0620004b4565b620000b5565b620004e4565b601290565b6001600160a01b038216620000e45760405163ec442f0560e01b81525f60048201526024015b60405180910390fd5b620000f15f8383620000f5565b5050565b6001600160a01b03831662000123578060025f828254620001179190620004ce565b90915550620001959050565b6001600160a01b0383165f9081526020819052604090205481811015620001775760405163391434e360e21b81526001600160a01b03851660048201526024810182905260448101839052606401620000db565b6001600160a01b0384165f9081526020819052604090209082900390555b6001600160a01b038216620001b357600280548290039055620001d1565b6001600160a01b0382165f9081526020819052604090208054820190555b816001600160a01b0316836001600160a01b03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040516200021791815260200190565b60405180910390a3505050565b634e487b7160e01b5f52604160045260245ffd5b600181811c908216806200024d57607f821691505b6020821081036200026c57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f821115620002bd57805f5260205f20601f840160051c81016020851015620002995750805b601f840160051c820191505b81811015620002ba575f8155600101620002a5565b50505b505050565b81516001600160401b03811115620002de57620002de62000224565b620002f681620002ef845462000238565b8462000272565b602080601f8311600181146200032c575f8415620003145750858301515b5f19600386901b1c1916600185901b17855562000386565b5f85815260208120601f198616915b828110156200035c578886015182559484019460019091019084016200033b565b50858210156200037a57878501515f19600388901b60f8161c191681555b505060018460011b0185555b505050505050565b634e487b7160e01b5f52601160045260245ffd5b600181815b80851115620003e257815f1904821115620003c657620003c66200038e565b80851615620003d457918102915b93841c9390800290620003a7565b509250929050565b5f82620003fa5750600162000497565b816200040857505f62000497565b81600181146200042157600281146200042c576200044c565b600191505062000497565b60ff8411156200044057620004406200038e565b50506001821b62000497565b5060208310610133831016604e8410600b841016171562000471575081810a62000497565b6200047d8383620003a2565b805f19048211156200049357620004936200038e565b0290505b92915050565b5f620004ad60ff841683620003ea565b9392505050565b80820281158282048414176200049757620004976200038e565b808201808211156200049757620004976200038e565b61075b80620004f25f395ff3fe608060405234801561000f575f80fd5b506004361061009b575f3560e01c806340c10f191161006357806340c10f191461011457806370a082311461012957806395d89b4114610151578063a9059cbb14610159578063dd62ed3e1461016c575f80fd5b806306fdde031461009f578063095ea7b3146100bd57806318160ddd146100e057806323b872dd146100f2578063313ce56714610105575b5f80fd5b6100a76101a4565b6040516100b491906105b5565b60405180910390f35b6100d06100cb36600461061c565b610234565b60405190151581526020016100b4565b6002545b6040519081526020016100b4565b6100d0610100366004610644565b61024d565b604051601281526020016100b4565b61012761012236600461061c565b610270565b005b6100e461013736600461067d565b6001600160a01b03165f9081526020819052604090205490565b6100a761027e565b6100d061016736600461061c565b61028d565b6100e461017a36600461069d565b6001600160a01b039182165f90815260016020908152604080832093909416825291909152205490565b6060600380546101b3906106ce565b80601f01602080910402602001604051908101604052809291908181526020018280546101df906106ce565b801561022a5780601f106102015761010080835404028352916020019161022a565b820191905f5260205f20905b81548152906001019060200180831161020d57829003601f168201915b5050505050905090565b5f3361024181858561029a565b60019150505b92915050565b5f3361025a8582856102ac565b61026585858561032c565b506001949350505050565b61027a8282610389565b5050565b6060600480546101b3906106ce565b5f3361024181858561032c565b6102a783838360016103bd565b505050565b6001600160a01b038381165f908152600160209081526040808320938616835292905220545f198114610326578181101561031857604051637dc7a0d960e11b81526001600160a01b038416600482015260248101829052604481018390526064015b60405180910390fd5b61032684848484035f6103bd565b50505050565b6001600160a01b03831661035557604051634b637e8f60e11b81525f600482015260240161030f565b6001600160a01b03821661037e5760405163ec442f0560e01b81525f600482015260240161030f565b6102a783838361048f565b6001600160a01b0382166103b25760405163ec442f0560e01b81525f600482015260240161030f565b61027a5f838361048f565b6001600160a01b0384166103e65760405163e602df0560e01b81525f600482015260240161030f565b6001600160a01b03831661040f57604051634a1406b160e11b81525f600482015260240161030f565b6001600160a01b038085165f908152600160209081526040808320938716835292905220829055801561032657826001600160a01b0316846001600160a01b03167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9258460405161048191815260200190565b60405180910390a350505050565b6001600160a01b0383166104b9578060025f8282546104ae9190610706565b909155506105299050565b6001600160a01b0383165f908152602081905260409020548181101561050b5760405163391434e360e21b81526001600160a01b0385166004820152602481018290526044810183905260640161030f565b6001600160a01b0384165f9081526020819052604090209082900390555b6001600160a01b03821661054557600280548290039055610563565b6001600160a01b0382165f9081526020819052604090208054820190555b816001600160a01b0316836001600160a01b03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040516105a891815260200190565b60405180910390a3505050565b5f602080835283518060208501525f5b818110156105e1578581018301518582016040015282016105c5565b505f604082860101526040601f19601f8301168501019250505092915050565b80356001600160a01b0381168114610617575f80fd5b919050565b5f806040838503121561062d575f80fd5b61063683610601565b946020939093013593505050565b5f805f60608486031215610656575f80fd5b61065f84610601565b925061066d60208501610601565b9150604084013590509250925092565b5f6020828403121561068d575f80fd5b61069682610601565b9392505050565b5f80604083850312156106ae575f80fd5b6106b783610601565b91506106c560208401610601565b90509250929050565b600181811c908216806106e257607f821691505b60208210810361070057634e487b7160e01b5f52602260045260245ffd5b50919050565b8082018082111561024757634e487b7160e01b5f52601160045260245ffdfea2646970667358221220bd208860fd106b5698b6c1bda6990e56127758a533369413e1f8a627750074dc64736f6c63430008160033";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected ETHSepoliaContractAbi(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ETHSepoliaContractAbi(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ETHSepoliaContractAbi(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ETHSepoliaContractAbi(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new Address(160, owner),
                new Address(160, spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(160, spender),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new Address(160, account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(String to, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MINT, 
                Arrays.<Type>asList(new Address(160, to),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> name() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Address(160, to),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new Address(160, from),
                new Address(160, to),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ETHSepoliaContractAbi load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ETHSepoliaContractAbi(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ETHSepoliaContractAbi load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ETHSepoliaContractAbi(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ETHSepoliaContractAbi load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ETHSepoliaContractAbi(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ETHSepoliaContractAbi load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ETHSepoliaContractAbi(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ETHSepoliaContractAbi> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ETHSepoliaContractAbi.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ETHSepoliaContractAbi> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ETHSepoliaContractAbi.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ETHSepoliaContractAbi> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ETHSepoliaContractAbi.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ETHSepoliaContractAbi> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ETHSepoliaContractAbi.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }
}

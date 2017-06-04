ns = [100 200 500 1000 2000];
ms = ns/5;
q_max = length(ns);
t_max = 10;
ks = 3*ones(q_max,1);

XL = zeros(t_max,q_max);
XL_t = zeros(t_max,q_max);
XIR = zeros(t_max,q_max);
XIR_t = zeros(t_max,q_max);

for q=1:q_max
    n = ns(q);
    m = ms(q);
    k = ks(q);
    for t=1:t_max
        q
        t
        S = zeros(n,m);
        for i=1:m
            kr = randi(n);
            r = randsample(n,kr);
            S(r,i) = 1;
        end

        f = [zeros(m,1); -ones(n,1)];
        A = [[-S eye(n)];[ones(1,m) zeros(1,n)]];
        b = [zeros(n,1); k];
        Aeq = [];
        beq = [];
        lb = zeros(m+n,1);
        ub = ones(m+n,1);
        
        tic;
        l = linprog(f,A,b,Aeq,beq,lb,ub);
        xl = l(1:m);
        yl = l(m+1:end);
        l_sol = sum(yl);
        XL(t,q) = l_sol;
        XL_t(t,q) = toc;
        
        tic
        mi = 1:m;
        xli = xl;
        xir = [];
        for i=1:k  % weighted sample without replacement
            r = randsample(mi,1,true,xli);
            [~,i] = ismember(r,mi);
            xir = [xir; r];
            mi = mi([1:i-1 i+1:end]);
            xli = xli([1:i-1 i+1:end]);
        end
        yir = sum(S(:,xir),2) >= 1;
        ir_sol = sum(yir);
        XIR(t,q) = ir_sol;
        XIR_t(t,q) = toc;
    end
end